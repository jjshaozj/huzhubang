USE [master]
GO
/****** Object:  Database [Seen]    Script Date: 2021/4/2 18:22:02 ******/
CREATE DATABASE [Seen]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Seen', FILENAME = N'D:\Code\database\seen\Seen.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Seen_log', FILENAME = N'D:\Code\database\seen\Seen_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [Seen] SET COMPATIBILITY_LEVEL = 130
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Seen].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Seen] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Seen] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Seen] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Seen] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Seen] SET ARITHABORT OFF 
GO
ALTER DATABASE [Seen] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Seen] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Seen] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Seen] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Seen] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Seen] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Seen] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Seen] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Seen] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Seen] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Seen] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Seen] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Seen] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Seen] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Seen] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Seen] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Seen] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Seen] SET RECOVERY FULL 
GO
ALTER DATABASE [Seen] SET  MULTI_USER 
GO
ALTER DATABASE [Seen] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Seen] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Seen] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Seen] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Seen] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Seen] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'Seen', N'ON'
GO
ALTER DATABASE [Seen] SET QUERY_STORE = OFF
GO
USE [Seen]
GO
/****** Object:  Table [dbo].[Comment]    Script Date: 2021/4/2 18:22:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Comment](
	[commentID] [varchar](20) NOT NULL,
	[tieID] [varchar](20) NULL,
	[content] [varchar](500) NULL,
	[c_userID] [varchar](20) NULL,
	[c_time] [datetime] NULL,
 CONSTRAINT [PK_评论] PRIMARY KEY NONCLUSTERED 
(
	[commentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Login]    Script Date: 2021/4/2 18:22:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Login](
	[userID] [varchar](20) NOT NULL,
	[nickname] [varchar](20) NULL,
	[password] [varchar](20) NULL,
	[sex] [bit] NULL,
	[HeadImage] [varchar](max) NULL,
	[signature] [varchar](500) NULL,
	[token] [varchar](500) NULL,
	[property1] [int] NULL,
	[property2] [int] NULL,
 CONSTRAINT [PK_用户] PRIMARY KEY NONCLUSTERED 
(
	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Picture]    Script Date: 2021/4/2 18:22:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Picture](
	[bottleID] [varchar](20) NOT NULL,
	[throwID] [varchar](50) NULL,
	[content] [varchar](500) NULL,
	[type] [int] NULL,
	[latitude] [varchar](15) NULL,
	[longitude] [varchar](15) NULL,
	[adress] [varchar](20) NULL,
	[time] [datetime] NULL,
	[IsVisual] [bit] NULL,
	[title] [varchar](50) NULL,
 CONSTRAINT [PK_瓶子] PRIMARY KEY NONCLUSTERED 
(
	[bottleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Tie]    Script Date: 2021/4/2 18:22:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Tie](
	[tieID] [varchar](20) NOT NULL,
	[t_userID] [varchar](20) NULL,
	[adress] [varchar](20) NULL,
	[time] [datetime] NULL,
	[title] [varchar](20) NULL,
	[content] [varchar](500) NULL,
	[pageviews] [float] NULL,
	[agree] [float] NULL,
	[Image1] [varchar](max) NULL,
	[Image2] [varchar](max) NULL,
	[Image3] [varchar](max) NULL,
 CONSTRAINT [PK_帖子] PRIMARY KEY NONCLUSTERED 
(
	[tieID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[Login] ADD  CONSTRAINT [DF_Login_userID]  DEFAULT ('   ') FOR [userID]
GO
ALTER TABLE [dbo].[Login] ADD  CONSTRAINT [DF_Login_nickname]  DEFAULT ('我的昵称') FOR [nickname]
GO
ALTER TABLE [dbo].[Login] ADD  CONSTRAINT [DF_Login_HeadImage]  DEFAULT ('') FOR [HeadImage]
GO
ALTER TABLE [dbo].[Login] ADD  CONSTRAINT [DF_Login_signature]  DEFAULT ('总想写点什么') FOR [signature]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_title]  DEFAULT ('无') FOR [title]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_pageviews]  DEFAULT ((0)) FOR [pageviews]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_agree]  DEFAULT ((0)) FOR [agree]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_Image1]  DEFAULT ('') FOR [Image1]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_Image2]  DEFAULT ('') FOR [Image2]
GO
ALTER TABLE [dbo].[Tie] ADD  CONSTRAINT [DF_Tie_Image3]  DEFAULT ('') FOR [Image3]
GO
USE [master]
GO
ALTER DATABASE [Seen] SET  READ_WRITE 
GO
