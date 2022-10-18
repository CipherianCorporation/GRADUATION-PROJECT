
CREATE DATABASE [shoeshy]
go
use [shoeshy]
go

CREATE TABLE [dbo].[categories] (
    [id]         INT            IDENTITY (100001, 1) NOT NULL,
    [name]       NVARCHAR (50)  NULL,
    [image_url]  NVARCHAR (255) NULL
    [updated_at] DATE           NULL,
    [created_at] DATE           DEFAULT (getdate()) NOT NULL,
    [deleted_at] DATE           NULL,
    CONSTRAINT [PK_categories] PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[sub_categories] (
    [id]            INT           IDENTITY (100001, 1) NOT NULL,
    [category_id]   INT           NOT NULL,
    [name]          NVARCHAR (50) NULL,
    [updated_at]    DATE          NULL,
    [created_at]    DATE          DEFAULT (getdate()) NOT NULL,
    [deleted_at]    DATE          NULL,
    CONSTRAINT [PK_sub_categories] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_31] FOREIGN KEY ([category_id]) REFERENCES [dbo].[categories] ([id]) ON DELETE CASCADE,

);

CREATE TABLE [dbo].[users] (
    [id]              INT            IDENTITY (100001, 1) NOT NULL,
    [username]        NVARCHAR (50)  NULL,
    [password]        NVARCHAR (255) NULL,
    [fullname]        NVARCHAR (255) NULL,
    [email]           NVARCHAR (255) NULL,
    [phone]           NVARCHAR (50)  NULL,
    [address]         NVARCHAR (255) NULL,
    [image_url]       NVARCHAR (255) NULL,
    [provider]        NVARCHAR (50)  NULL,
    [provider_id]     NVARCHAR (50)  NULL,
    [enabled]         BIT            DEFAULT ((0)) NULL,
    [verify_code]     NVARCHAR (255) NULL,
    [reset_pwd_token] NVARCHAR (255) NULL,
    [updated_at]      DATE           NULL,
    [created_at]      DATE           DEFAULT (getdate()) NOT NULL,
    [deleted_at]      DATE           NULL,
    CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED ([id] ASC)
);

drop table products

CREATE TABLE [dbo].[products] (
    [id]              INT            IDENTITY (100001, 1) NOT NULL,
    [name]            NVARCHAR (50)  NOT NULL,
    [image]           NVARCHAR (50)  DEFAULT (N'default-product.jpg') NOT NULL,
    [price]           FLOAT (53)     DEFAULT ((0)) NOT NULL,
    [available]       BIT            DEFAULT ((1)) NOT NULL,
    [color]           NVARCHAR (50)  NOT NULL,
    [size]            INT            NULL,
    [sale_off]        FLOAT (53)     DEFAULT ((0)) NULL,
    [sold]            BIGINT         DEFAULT ((0)) NOT NULL,
    [description]     NVARCHAR (255) DEFAULT (N'') NULL,
    [category_id]     INT            NOT NULL,
    [sub_category_id] INT            NOT NULL,
    [created_by]      INT            NOT NULL,
    [updated_at]      DATE           NULL,
    [created_at]      DATE           DEFAULT (getdate()) NOT NULL,
    [deleted_at]      DATE           NULL,
    CONSTRAINT [PK_products] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_33] FOREIGN KEY ([sub_category_id]) REFERENCES [dbo].[sub_categories] ([id]),
    CONSTRAINT [FK_7] FOREIGN KEY ([created_by]) REFERENCES [dbo].[users] ([id]),
    CONSTRAINT [FK_9] FOREIGN KEY ([category_id]) REFERENCES [dbo].[categories] ([id]) ON DELETE CASCADE
);

CREATE TABLE [dbo].[roles] (
    [id]         NVARCHAR (50) NOT NULL,
    [name]       NVARCHAR (50) NOT NULL,
    [updated_at] DATE          NULL,
    [created_at] DATE          DEFAULT (getdate()) NOT NULL,
    [deleted_at] DATE          NULL,
    CONSTRAINT [PK_roles] PRIMARY KEY CLUSTERED ([id] ASC)
);


CREATE TABLE [dbo].[user_roles] (
    [id]         INT           IDENTITY (100001, 1) NOT NULL,
    [user_id]    INT           NOT NULL,
    [role_id]    NVARCHAR (50) NOT NULL,
    [updated_at] DATE          NULL,
    [created_at] DATE          DEFAULT (getdate()) NOT NULL,
    [deleted_at] DATE          NULL,
    CONSTRAINT [PK_users_role] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_1] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT [FK_2] FOREIGN KEY ([role_id]) REFERENCES [dbo].[roles] ([id]) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE [dbo].[vouchers] (
    [id]          INT            IDENTITY (1000001, 1) NOT NULL,
    [code]        NVARCHAR (255) NOT NULL,
    [description] NVARCHAR (255) DEFAULT '' NOT NULL,
    [value]       FLOAT (53)     DEFAULT ((0)) NOT NULL,
    [start_date]  DATE           DEFAULT (getdate()) NOT NULL,
    [end_date]    DATE           NOT NULL,
    [updated_at]  DATE           NULL,
    CONSTRAINT [PK_vouchers] PRIMARY KEY CLUSTERED ([id] ASC),
);

CREATE TABLE [dbo].[orders] (
    [id]             BIGINT         IDENTITY (100001, 1) NOT NULL,
    [address]        NVARCHAR (100) NOT NULL,
    [payment_method] NVARCHAR (50)  DEFAULT ('') NOT NULL,
    [order_status]   NVARCHAR (50)  DEFAULT ('') NOT NULL,
    [user_id]        INT            NOT NULL,
    [voucher_id]     INT            NULL,
    [total]          FLOAT (53)     DEFAULT ((0)) NOT NULL,
    [updated_at]     INT            NULL,
    [created_at]     DATE           DEFAULT (getdate()) NOT NULL,
    [deleted_at]     INT            NULL,
    CONSTRAINT [PK_orders] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_4] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT [FK_71] FOREIGN KEY ([voucher_id]) REFERENCES [dbo].[vouchers] ([id]) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE [dbo].[order_details] (
    [id]         BIGINT     IDENTITY (100001, 1) NOT NULL,
    [price]      FLOAT (53) DEFAULT ((0)) NOT NULL,
    [quantity]   INT        DEFAULT ((1)) NOT NULL,
    [product_id] INT        NOT NULL,
    [order_id]   BIGINT     NOT NULL,
    [updated_at] DATE       NULL,
    [created_at] DATE       DEFAULT (getdate()) NOT NULL,
    [deleted_at] DATE       NULL,
    CONSTRAINT [PK_order_details] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_5] FOREIGN KEY ([product_id]) REFERENCES [dbo].[products] ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT [FK_6] FOREIGN KEY ([order_id]) REFERENCES [dbo].[orders] ([id]) ON DELETE CASCADE ON UPDATE CASCADE
);


-- insert to database

INSERT [dbo].[categories] ([name]) VALUES (N'Giày nam')
INSERT [dbo].[categories] ([name]) VALUES (N'Giày nữ')
INSERT [dbo].[categories] ([name]) VALUES (N'Phụ kiện')

INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100001',N'Giày thể thao nam')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100001',N'Giày tây & slippon')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100001',N'Dép nam')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100001',N'Sandal nam')

INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100002',N'Giày thể thao nữ')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100002',N'Giày cao gót')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100002',N'Sandal nữ')
INSERT [dbo].[sub_categories] ([category_id], [name]) VALUES ('100002',N'Dép nữ')

select c.id as 'CategoryID', c.name, s.id as 'SubCatesID',s.name from categories c inner join sub_categories s 
on c.id = s.category_id 

select * from categories
select * from sub_categories
select * from products p WHERE p.price BETWEEN '0' AND '100000'
select * from users
select * from roles
select * from user_roles
select * from orders
select * from order_details


ALTER TABLE users
ADD Email varchar(255);