package com.edu.graduationproject.handler;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.edu.graduationproject.entity.Visitor;
import com.edu.graduationproject.service.VisitorService;
import com.edu.graduationproject.utils.HttpRequestResponseUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitorLogger implements HandlerInterceptor {

    // source:
    // https://roytuts.com/how-to-log-online-user-activity-in-spring-boot-applications/
    // https://www.baeldung.com/geolocation-by-ip-with-maxmind
    // https://boottechnologies-ci.medium.com/spring-boot-geolocation-by-ip-using-geolite2-database-e4ee95ef677

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private DatabaseReader databaseReader;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                try {
                    final String ip = HttpRequestResponseUtils.getClientIpAddress();
                    final String url = HttpRequestResponseUtils.getRequestUrl();
                    final String page = HttpRequestResponseUtils.getRequestUri();
                    final String refererPage = HttpRequestResponseUtils.getRefererPage();
                    final String queryString = HttpRequestResponseUtils.getPageQueryString();
                    final String userAgent = HttpRequestResponseUtils.getUserAgent();
                    final String requestMethod = HttpRequestResponseUtils.getRequestMethod();
                    final LocalDateTime timestamp = LocalDateTime.now();
            
                    Visitor visitor = new Visitor();
                    visitor.setUserInfo(HttpRequestResponseUtils.getLoggedInUser());
                    visitor.setIp(ip);
                    visitor.setMethod(requestMethod);
                    visitor.setUrl(url);
                    visitor.setPage(page);
                    visitor.setQueryString(queryString);
                    visitor.setRefererPage(refererPage);
                    visitor.setUserAgent(userAgent);
                    visitor.setLoggedTime(timestamp);
            
                    List<Visitor> list = visitorService.findAllByIp(ip).orElse(null);
                    if (list.isEmpty() || list == null) {
                        visitor.setUniqueVisit(true);
                    } else {
                        visitor.setUniqueVisit(false);
                    }
            
                    if (!ip.equals("0:0:0:0:0:0:0:1")) {
                        InetAddress inetAddress = InetAddress.getByName(ip);
                        CityResponse cityResponse = databaseReader.city(inetAddress);
                        if (cityResponse != null && cityResponse.getCity() != null) {
                            String continent = (cityResponse.getContinent() != null) ? cityResponse.getContinent().getName() : "";
                            String country = (cityResponse.getCountry() != null) ? cityResponse.getCountry().getName() : "";
                            String location = String.format("%s %s %s", continent, country, cityResponse.getCity().getName());
            
                            visitor.setCity(cityResponse.getCity().getName());
                            visitor.setFullLocation(location);
                            visitor.setLatitude(cityResponse.getLocation() != null ? cityResponse.getLocation().getLatitude() : 0);
                            visitor.setLongtitude(
                                    cityResponse.getLocation() != null ? cityResponse.getLocation().getLongitude() : 0);
                        }
                    }
                    visitorService.saveVisitorInfo(visitor);
                } catch (Exception e) {
                    log.error("Error in VisitorLogger.preHandle: " + e.getMessage());
                }
        return true;
    }
}
