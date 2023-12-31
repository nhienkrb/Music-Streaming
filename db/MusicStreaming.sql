﻿create database MusicStreaming
use MusicStreaming

CREATE TABLE IMAGES (
	ACCESSID  NVARCHAR(100) PRIMARY KEY,
	URL VARCHAR(MAX),
	PUBLICID VARCHAR(MAX),
	HEIGHT INT,
	WEIGHT INT
);
CREATE TABLE ROLE (
	IDROLE INT IDENTITY(1,1) PRIMARY KEY,
	NAMEROLE VARCHAR(35)
);

CREATE TABLE ACCOUNTS (
	EMAIL VARCHAR(255) PRIMARY KEY,
	PASSWORD VARCHAR(500),
	USENAME NVARCHAR(55),
	BIRTHDAY DATE,
	GENDER INT,
	COUNTRY NVARCHAR(55),
	ISVERIFY BIT,
	VERIFYCATIONCODE VARCHAR(250),
	VERIFYCATIONCODEEXPIRES DATETIME,
	REMAININGVERIFYCATION INT DEFAULT 3,
	ISBLOCKED BIT DEFAULT 0,
	REFRESHTOKEN VARCHAR(max),
	IMAGEID NVARCHAR(100),
	
	FOREIGN KEY (IMAGEID) REFERENCES IMAGES(ACCESSID) ON DELETE SET NULL ON UPDATE CASCADE,
);

CREATE TABLE AUTHOR (
	AUTHORID BIGINT IDENTITY(1,1) PRIMARY KEY,
	IDROLE INT,
	EMAIL VARCHAR(255),

	FOREIGN KEY (IDROLE) REFERENCES ROLE(IDROLE) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EMAIL) REFERENCES ACCOUNTS(EMAIL) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE FOLLOWER (
	FOLLOWERID INT IDENTITY(1,1) PRIMARY KEY,
	FOLLOWDATE DATE,
	ACCOUNT_A BIGINT,
	ACCOUNT_B BIGINT,

	FOREIGN KEY (ACCOUNT_A) REFERENCES AUTHOR(AUTHORID),
	FOREIGN KEY (ACCOUNT_B) REFERENCES AUTHOR(AUTHORID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE SUBCRIPTIONS (
	SUBCRIPTIONID BIGINT IDENTITY(1,1) PRIMARY KEY,
	SUBCRIPTIONTYPE VARCHAR(100),
	PRICE FLOAT,
	PRDSTRIPEID VARCHAR(500),
	PRDPAYPALID VARCHAR(500),
	PLAYLISTALLOW INT,/*quantity playlist*/
	NIP INT, /*Number in playlist*/
	DESCRIPTION NVARCHAR(500),
	CREATEDATE DATE,
	DURATION INT,
);

CREATE TABLE USERTYPE (
	USERTYPEID BIGINT IDENTITY(1,1) PRIMARY KEY,
	NAMETYPE VARCHAR(20),
	STARTDATE DATE,
	ENDDATE DATE,
	STATUS NVARCHAR(55),
	PAYMENTSTATUS INT,
	ACCOUNTID VARCHAR(255),
	SUBCRIPTIONID BIGINT,

	FOREIGN KEY (ACCOUNTID) REFERENCES ACCOUNTS(EMAIL) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (SUBCRIPTIONID) REFERENCES SUBCRIPTIONS(SUBCRIPTIONID) ON DELETE CASCADE ON UPDATE CASCADE,

);

CREATE TABLE ARTIST (
	ARTISTID BIGINT IDENTITY(1,1) PRIMARY KEY,
	ARTISTNAME NVARCHAR(55),
	DATEOFBIRTH DATE,
	FULLNAME NVARCHAR(55),
	PLACEOFBIRTH NVARCHAR(55),
	BIO NVARCHAR(max),
	IMAGEGALLERY VARCHAR(500),
	PUBLICIDIMAGEGALLERY VARCHAR(MAX),
	SOCIALMEDIALINKS VARCHAR(255),
	ACTIVE NVARCHAR(55),
	DATESTARTED DATE,
	VERIFY BIT,
	PROFILEIMAGE NVARCHAR(100),
	BACKGROUDIMAGE NVARCHAR(100),
	EMAIL VARCHAR(255),

	FOREIGN KEY (EMAIL) REFERENCES ACCOUNTS(EMAIL) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (PROFILEIMAGE) REFERENCES IMAGES(ACCESSID) ON DELETE  NO ACTION,
	FOREIGN KEY (BACKGROUDIMAGE) REFERENCES IMAGES(ACCESSID) ON DELETE  NO ACTION,
);

CREATE TABLE SONGS (
	SONGSID BIGINT IDENTITY(1,1) PRIMARY KEY,
	SONGNAME NVARCHAR(55),
	IMAGEID NVARCHAR(100),
	REALEASEDAY DATETIME,
	ISDELETED BIT,
	DESCRIPTIONS NVARCHAR(max),
	ARTISTCREATE BIGINT,
	FOREIGN KEY (IMAGEID) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
);

CREATE TABLE WRITTER (
	WRITTERID BIGINT IDENTITY(1,1) PRIMARY KEY,
	ARTISTID BIGINT,
	SONGSID BIGINT,

	FOREIGN KEY (ARTISTID) REFERENCES ARTIST(ARTISTID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (SONGSID) REFERENCES SONGS(SONGSID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE GENRE (
	ID INT IDENTITY(1,1) PRIMARY KEY,
	NAMEGENRE VARCHAR(30)
);

CREATE TABLE RECORDING (
	RECORDINGID BIGINT IDENTITY(1,1) PRIMARY KEY,
	RECORDINGIDNAME NVARCHAR(55),
	AUDIOFILEURL VARCHAR(MAX),
	PUBLICIDAUDIOFILE VARCHAR(MAX),
	LYRICSURL NVARCHAR(MAX),
	PUBLICIDLYRICS VARCHAR(MAX),
	LISTENED BIGINT,
	SONGSTYLE NVARCHAR(55),
	MOOD NVARCHAR(55),
	CULTURE NVARCHAR(55),
	INSTRUMENT NVARCHAR(255),
	VERSIONS NVARCHAR(55),
	STUDIO NVARCHAR(55),
	IDMV VARCHAR(200),
	PRODUCE NVARCHAR(55),
	RECORDINGDATE DATE,
	ISDELETED BIT,
	SONGSID BIGINT,
	EMAILCREATE VARCHAR(255),
	FOREIGN KEY (SONGSID) REFERENCES SONGS(SONGSID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE MONITOR(
	INSTRUMENTID BIGINT IDENTITY(1,1) PRIMARY KEY,
	ACCOUNT VARCHAR(255),
	RECORDINGID BIGINT,

	FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS(EMAIL) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	FOREIGN KEY (RECORDINGID) REFERENCES RECORDING(RECORDINGID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE SONGGENRE(
	ID BIGINT IDENTITY(1,1) PRIMARY KEY,
	IDGENRE INT,
	IDRECORD BIGINT,

	FOREIGN KEY (IDGENRE) REFERENCES GENRE(ID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (IDRECORD) REFERENCES RECORDING(RECORDINGID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE WISHLIST (
	WISHLISTID INT IDENTITY(1,1) PRIMARY KEY,
	ADDDATE DATE,
	USERTYPEID BIGINT,
	RECORDINGID BIGINT,

	FOREIGN KEY (RECORDINGID) REFERENCES RECORDING(RECORDINGID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (USERTYPEID) REFERENCES USERTYPE(USERTYPEID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE ADVERTISEMENT (
	IDADS BIGINT IDENTITY(1,1) PRIMARY KEY,
	TITLE NVARCHAR(255),
	CONTENT NVARCHAR(255),
	URL NVARCHAR(2000),
	STARTDATE DATE,
	ENDDATE DATE,
	BUDGET FLOAT,
	STATUS NVARCHAR(55),
	TARGETAUDIENCE NVARCHAR(55),
	CLICKED BIGINT,
	TAG VARCHAR(55),
	PRIORITY INT,
	BANNER NVARCHAR(100),
	ACCOUNTID VARCHAR(255),
	AUDIOFILE NVARCHAR(2000),
	PUBLICIDAUDIO NVARCHAR(2000),
	FOREIGN KEY (ACCOUNTID) REFERENCES ACCOUNTS(EMAIL) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY (BANNER) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
	
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);

CREATE TABLE NEWS (
	IDNEWS BIGINT IDENTITY(1,1) PRIMARY KEY,
	TITLE NVARCHAR(255),
	CONTENT ntext,
	IMAGE NVARCHAR(100),
	PUBLISHDATE DATE,
	LASTMODIFIED DATETIME,
	AUTHORID VARCHAR(255),
	CREATEFOR VARCHAR(20),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,

	FOREIGN KEY (AUTHORID) REFERENCES ACCOUNTS(EMAIL) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	FOREIGN KEY (IMAGE) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
);

CREATE TABLE TAGS (
	TAGID BIGINT IDENTITY(1,1) PRIMARY KEY,
	NAMETAG NVARCHAR(55),

	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);

CREATE TABLE PODCAST (
	PODCASTID BIGINT IDENTITY(1,1) PRIMARY KEY,
	PODCASTNAME NVARCHAR(55),
	BIO NVARCHAR(max),
	SOCIALMEDIALINK VARCHAR(1000),
	RELEASEDATE DATE,
	AUTHORNAME NVARCHAR(100),
	LANGUAGE NVARCHAR(55),
	IMGAGEID NVARCHAR(100),
	CATEGORY BIGINT,
	ACCOUNTID VARCHAR(255),
	RATE BIGINT,

	FOREIGN KEY (ACCOUNTID) REFERENCES ACCOUNTS(EMAIL) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY (IMGAGEID) REFERENCES IMAGES(ACCESSID)ON DELETE NO ACTION ,	
	FOREIGN KEY (CATEGORY) REFERENCES TAGS(TAGID) ON DELETE NO ACTION ON UPDATE CASCADE,
);


CREATE TABLE EPISODES (
	EPISODESID BIGINT IDENTITY(1,1) PRIMARY KEY,
	FILEURL NVARCHAR(MAX),
	PUBLICIDFILE NVARCHAR(MAX),
	EPISODESTITLE NVARCHAR(1000),
	DESCRIPTIONS NVARCHAR(1000),
	PUBLISHDATE DATETIME,
	SESSIONNUMBER INT,
	EPNUMBER INT,
	EPTYPE NVARCHAR(55),
	CONTENT NVARCHAR(55),
	ISPUBLIC BIT,
	ISDELETED BIT,
	PODCASTID BIGINT,
	IMAGEEP NVARCHAR(100),
	LISTENED BIGINT,
	FOREIGN KEY (IMAGEEP) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
	FOREIGN KEY (PODCASTID) REFERENCES PODCAST(PODCASTID) ON DELETE CASCADE ON UPDATE CASCADE,
);


CREATE TABLE REPORT (
	REPORTID INT IDENTITY(1,1) PRIMARY KEY,
	USERTYPEID BIGINT,
	REPORTDATE DATE,
	DESCRIPTION NVARCHAR(55),
	ARTISTID BIGINT,
	RECORDINGID BIGINT,
	POSTCASTID BIGINT,
	EPISODESID BIGINT,

	FOREIGN KEY (USERTYPEID) REFERENCES USERTYPE(USERTYPEID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EPISODESID) REFERENCES EPISODES(EPISODESID) ON DELETE NO ACTION,
	FOREIGN KEY (POSTCASTID) REFERENCES PODCAST(PODCASTID)  ON DELETE NO ACTION,
	FOREIGN KEY (RECORDINGID) REFERENCES RECORDING(RECORDINGID)  ON DELETE NO ACTION,
	FOREIGN KEY (ARTISTID) REFERENCES ARTIST(ARTISTID)  ON DELETE NO ACTION,
	
);

CREATE TABLE PLAYLISTS (
	PLAYLISTID BIGINT IDENTITY(1,1) PRIMARY KEY,
	PLAYLISTNAME NVARCHAR(55),
	QUANTITY INT,
	ISPUBLIC BIT,
	STATUS VARCHAR(55),
	CREATDATE DATE,
	USERTYPEID BIGINT,
	IMAGE NVARCHAR(100),

	FOREIGN KEY (IMAGE) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
	FOREIGN KEY (USERTYPEID) REFERENCES USERTYPE(USERTYPEID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE PLAYLIST_PODCAST (
	PLAYLIST_PODCASTID BIGINT IDENTITY(1,1) PRIMARY KEY,
	PLAYLISTID BIGINT,
	EPISODESID BIGINT,

	FOREIGN KEY (PLAYLISTID) REFERENCES PLAYLISTS(PLAYLISTID) ON DELETE CASCADE ON UPDATE NO ACTION,
	FOREIGN KEY (EPISODESID) REFERENCES EPISODES(EPISODESID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE PLAYLIST_RECORDING (
	PLAYLIST_RECORDINGID BIGINT IDENTITY(1,1) PRIMARY KEY,
	RECORDINGID BIGINT,
	PLAYLISTSID BIGINT,

	FOREIGN KEY (RECORDINGID) REFERENCES RECORDING(RECORDINGID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (PLAYLISTSID) REFERENCES PLAYLISTS(PLAYLISTID) ON DELETE CASCADE ON UPDATE CASCADE,
);


CREATE TABLE ALBUM (
	ALBUMID BIGINT IDENTITY(1,1) PRIMARY KEY,
	ALBUMNAME NVARCHAR(55),
	RELEASEDATE DATETIME,
	COVERIMAGE NVARCHAR(100),
	ARTISTID BIGINT,
	DESCRIPTIONS NVARCHAR(max),
	FOREIGN KEY (COVERIMAGE) REFERENCES IMAGES(ACCESSID) ON DELETE NO ACTION,
	FOREIGN KEY (ARTISTID) REFERENCES ARTIST(ARTISTID) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE TRACK (
	TRACKID BIGINT IDENTITY(1,1) PRIMARY KEY,
	ALBUMID BIGINT,
	RECORDINGID BIGINT,
	TRACKNUMBER INT,

	FOREIGN KEY (RECORDINGID) REFERENCES RECORDING(RECORDINGID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (ALBUMID) REFERENCES ALBUM(ALBUMID)ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE ACCESS (
	ACCESSID INT IDENTITY(1,1) PRIMARY KEY,
	PLAYLIST_RECORDINGID BIGINT,
	USERTYPEID BIGINT,
	EPISODESID BIGINT,

	FOREIGN KEY (EPISODESID) REFERENCES PLAYLIST_PODCAST(PLAYLIST_PODCASTID),
	FOREIGN KEY (USERTYPEID) REFERENCES USERTYPE(USERTYPEID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (PLAYLIST_RECORDINGID) REFERENCES PLAYLIST_RECORDING(PLAYLIST_RECORDINGID)
);

CREATE TABLE SONG_STYLE (
	SONG_STYLEID INT IDENTITY(1,1) PRIMARY KEY,
	SONG_STYLENAME NVARCHAR(55),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);

CREATE TABLE MOOD (
	MOODID INT IDENTITY(1,1) PRIMARY KEY,
	MOODNAME NVARCHAR(55),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME
);

CREATE TABLE INSTRUMENT (
	INSTRUMENTID INT IDENTITY(1,1) PRIMARY KEY,
	INSTRUMENTNAME NVARCHAR(55),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME
);

CREATE TABLE CULTURE (
	CULTUREID INT IDENTITY(1,1) PRIMARY KEY,
	CULTURENAME NVARCHAR(55),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);


CREATE TABLE NOTIFICATION (
	NOTIFICATIONID INT IDENTITY(1,1) PRIMARY KEY,
	TITILE NVARCHAR(50),
	CONTENT NVARCHAR(250),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME
);

CREATE TABLE SLIDE (
	SLIDEID INT IDENTITY(1,1) PRIMARY KEY,
	POSITION NVARCHAR(55),
	LISTIMAGE VARCHAR(55),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);

CREATE TABLE COUNTRY (
	ID VARCHAR(15) PRIMARY KEY,
	NAMECOUNTRY VARCHAR(30),
	CREATEBY NVARCHAR(255),
	CREATEDATE DATETIME,
	MODIFIEDBY NVARCHAR(255),
	MODIFIDATE DATETIME,
);
						


INSERT INTO COUNTRY (ID, NAMECOUNTRY, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('VN', 'Vietnam', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('US', 'USA', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('CA', 'Canada', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO SLIDE (POSITION, LISTIMAGE, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Slide 1', 'image1.jpg', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Slide 2', 'image2.jpg', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Slide 3', 'image3.jpg', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO NOTIFICATION (TITILE, CONTENT, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Thông báo 1', 'Nội dung thông báo 1', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Thông báo 2', 'Nội dung thông báo 2', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Thông báo 3', 'Nội dung thông báo 3', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO CULTURE (CULTURENAME, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Culture 1', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Culture 2', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Culture 3', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO INSTRUMENT (INSTRUMENTNAME, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Instrument 1', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Instrument 2', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Instrument 3', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO MOOD (MOODNAME, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Happy', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Sad', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Angry', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO SONG_STYLE (SONG_STYLENAME, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Pop', 'Admin', '2023-10-25 08:00:00', 'Editor', '2023-10-25 09:30:00'),
('Rock', 'Admin', '2023-10-25 08:15:00', 'Editor', '2023-10-25 09:45:00'),
('Hip-Hop', 'Admin', '2023-10-25 08:30:00', 'Editor', '2023-10-25 10:00:00');


INSERT INTO ACCESS (PLAYLIST_RECORDINGID, USERTYPEID, EPISODESID)
VALUES 
(1, 1, 1),
(2, 2, 2),
(3, 3, 3);


INSERT INTO TRACK (ALBUMID, RECORDINGID, TRACKNUMBER)
VALUES 
(1, 1, 1),
(1, 2, 2),
(2, 3, 1);


INSERT INTO ALBUM (ALBUMNAME, RELEASEDATE, COVERIMAGE, ARTISTID, DESCRIPTIONS)
VALUES 
('Album 1', '2023-10-25 08:00:00', 'Avatar/System/807831_rrsd2v.png', 3, 'Mô tả album 1'),
('Album 2', '2023-10-25 08:15:00', 'Avatar/System/807831_rrsd2v.png', 4, 'Mô tả album 2'),
('Album 3', '2023-10-25 08:30:00', 'Avatar/System/807831_rrsd2v.png', 3, 'Mô tả album 3');


INSERT INTO PLAYLIST_RECORDING (RECORDINGID, PLAYLISTSID)
VALUES 
(1, 1),
(2, 2),
(3, 3);


INSERT INTO PLAYLIST_PODCAST (PLAYLISTID, EPISODESID)
VALUES 
(1, 1),
(2, 2),
(3, 3);


INSERT INTO REPORT (USERTYPEID, REPORTDATE, DESCRIPTION, ARTISTID, RECORDINGID, POSTCASTID, EPISODESID)
VALUES 
(1, '2023-10-25', 'Mô tả báo cáo 1', 3, 1, NULL, 1),
(2, '2023-10-26', 'Mô tả báo cáo 2', 4, 2, NULL, 2),
(3, '2023-10-27', 'Mô tả báo cáo 3', 3, 3, NULL, 1);


INSERT INTO PLAYLISTS (PLAYLISTNAME, QUANTITY, ISPUBLIC, STATUS, CREATDATE, USERTYPEID, IMAGE)
VALUES 
('Playlist 1', 10, 1, 'Active', '2023-10-25', 1, 'Avatar/System/807831_rrsd2v.png'),
('Playlist 2', 15, 0, 'Inactive', '2023-10-26', 2, 'Avatar/System/807831_rrsd2v.png'),
('Playlist 3', 20, 1, 'Active', '2023-10-27', 3, 'Avatar/System/807831_rrsd2v.png');


INSERT INTO EPISODES (FILEURL, PUBLICIDFILE, EPISODESTITLE, DESCRIPTIONS, PUBLISHDATE, SESSIONNUMBER, EPNUMBER, EPTYPE, CONTENT, ISPUBLIC, PODCASTID, IMAGEEP)
VALUES 
('fileurl1', 'publicfile1', 'Episode 1', 'Description 1', '2023-10-25', 1, 1, 'Type 1', 'Content 1', 1, 1, 'Avatar/System/807831_rrsd2v.png'),
('fileurl2', 'publicfile2', 'Episode 2', 'Description 2', '2023-10-26', 2, 2, 'Type 2', 'Content 2', 1, 2, 'Avatar/System/807831_rrsd2v.png'),
('fileurl3', 'publicfile3', 'Episode 3', 'Description 3', '2023-10-27', 3, 3, 'Type 3', 'Content 3', 0, 3, 'Avatar/System/807831_rrsd2v.png');


INSERT INTO PODCAST (PODCASTNAME, BIO, SOCIALMEDIALINK, RELEASEDATE, LANGUAGE, IMGAGEID, CATEGORY, ACCOUNTID, RATE)
VALUES 
('Podcast 1', 'Bio 1', 'Social Media 1', '2023-10-25', 'Language 1', 'Avatar/System/807831_rrsd2v.png', 1, 'jvke@gmail.com', 5),
('Podcast 2', 'Bio 2', 'Social Media 2', '2023-10-26', 'Language 2', 'Avatar/System/807831_rrsd2v.png', 2, 'mck@gmail.com', 4),
('Podcast 3', 'Bio 3', 'Social Media 3', '2023-10-27', 'Language 3', 'Avatar/System/807831_rrsd2v.png', 3, 'khoandps24828@fpt.edu.vn', 3);


INSERT INTO TAGS (NAMETAG, CREATEBY, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Tag 1', 'User 1', '2023-10-25 08:00:00', 'User 1', '2023-10-25 08:00:00'),
('Tag 2', 'User 2', '2023-10-26 09:00:00', 'User 2', '2023-10-26 09:00:00'),
('Tag 3', 'User 3', '2023-10-27 10:00:00', 'User 3', '2023-10-27 10:00:00');


INSERT INTO NEWS (TITLE, CONTENT, IMAGE, PUBLISHDATE, LASTMODIFIED, AUTHORID, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('News 1', 'Content 1', 'Avatar/System/807831_rrsd2v.png', '2023-10-25', '2023-10-25 08:00:00', 'jvke@gmail.com', '2023-10-25 08:00:00', 'User 1', '2023-10-25 08:00:00'),
('News 2', 'Content 2', 'Avatar/System/807831_rrsd2v.png', '2023-10-26', '2023-10-26 09:00:00', 'mck@gmail.com', '2023-10-26 09:00:00', 'User 2', '2023-10-26 09:00:00'),
('News 3', 'Content 3', 'Avatar/System/807831_rrsd2v.png', '2023-10-27', '2023-10-27 10:00:00', 'khoandps24828@fpt.edu.vn', '2023-10-27 10:00:00', 'User 3', '2023-10-27 10:00:00');


INSERT INTO ADVERTISEMENT (TITLE, CONTENT, URL, STARTDATE, ENDDATE, BUDGET, STATUS, TARGETAUDIENCE, CLICKED, TAG, PRIORITY, BANNER, ACCOUNTID, AUDIOFILE, PUBLICIDAUDIO, CREATEDATE, MODIFIEDBY, MODIFIDATE)
VALUES 
('Ad 1', 'Content 1', 'url1', '2023-10-25', '2023-10-26', 100.0, 'Active', 'Audience 1', 100, 'Tag 1', 1, 'Avatar/System/807831_rrsd2v.png', 'jvke@gmail.com', 'audio1', 'publicaudio1', '2023-10-25 08:00:00', 'User 1', '2023-10-25 08:00:00'),
('Ad 2', 'Content 2', 'url2', '2023-10-26', '2023-10-27', 150.0, 'Inactive', 'Audience 2', 200, 'Tag 2', 2, 'Avatar/System/807831_rrsd2v.png', 'mck@gmail.com', 'audio2', 'publicaudio2', '2023-10-26 09:00:00', 'User 2', '2023-10-26 09:00:00'),
('Ad 3', 'Content 3', 'url3', '2023-10-27', '2023-10-28', 200.0, 'Active', 'Audience 3', 300, 'Tag 3', 3, 'Avatar/System/807831_rrsd2v.png', 'khoandps24828@fpt.edu.vn', 'audio3', 'publicaudio3', '2023-10-27 10:00:00', 'User 3', '2023-10-27 10:00:00');


INSERT INTO WISHLIST (ADDDATE, USERTYPEID, RECORDINGID)
VALUES 
('2023-10-25', 1, 1),
('2023-10-26', 2, 2),
('2023-10-27', 3, 3);


INSERT INTO SONGGENRE (IDGENRE, IDRECORD)
VALUES 
(1, 1),
(2, 2),
(3, 3);


INSERT INTO MONITOR (ACCOUNT, RECORDINGID)
VALUES 
('jvke@gmail.com', 1),
('mck@gmail.com', 2),
('khoandps24828@fpt.edu.vn', 3);


INSERT INTO RECORDING (RECORDINGIDNAME, AUDIOFILEURL, PUBLICIDAUDIOFILE, LYRICSURL, PUBLICIDLYRICS, LIKES, DURATION, SONGSTYLE, MOOD, CULTURE, INSTRUMENT, VERSIONS, STUDIO, IDMV, PRODUCE, RECORDINGDATE, ISDELETED, SONGSID, EMAILCREATE)
VALUES 
('Recording 1', 'audiofile1.mp3', 'publicaudiofile1', 'lyrics1.txt', 'publiclyrics1', 100, 240, 'Style 1', 'Mood 1', 'Culture 1', 'Instrument 1', 'Version 1', 'Studio 1', 'MV001', 'Producer 1', '2023-10-25', 0, 1, 'user1@example.com'),
('Recording 2', 'audiofile2.mp3', 'publicaudiofile2', 'lyrics2.txt', 'publiclyrics2', 150, 180, 'Style 2', 'Mood 2', 'Culture 2', 'Instrument 2', 'Version 2', 'Studio 2', 'MV002', 'Producer 2', '2023-10-26', 0, 2, 'user2@example.com'),
('Recording 3', 'audiofile3.mp3', 'publicaudiofile3', 'lyrics3.txt', 'publiclyrics3', 200, 210, 'Style 3', 'Mood 3', 'Culture 3', 'Instrument 3', 'Version 3', 'Studio 3', 'MV003', 'Producer 3', '2023-10-27', 1, 3, 'user3@example.com');


INSERT INTO GENRE (NAMEGENRE)
VALUES 
('Genre 1'),
('Genre 2'),
('Genre 3');


INSERT INTO WRITTER (ARTISTID, SONGSID)
VALUES 
(3, 1),
(4, 2),
(3, 3);


INSERT INTO SONGS (SONGNAME, IMAGEID, REALEASEDAY, ISDELETED, DESCRIPTIONS, ARTISTCREATE)
VALUES 
('Song 1', 'Avatar/System/807831_rrsd2v.png', '2023-10-25', 0, 'Description 1', 1),
('Song 2', 'Avatar/System/807831_rrsd2v.png', '2023-10-26', 0, 'Description 2', 2),
('Song 3', 'Avatar/System/807831_rrsd2v.png', '2023-10-27', 1, 'Description 3', 3);


INSERT INTO ARTIST (ARTISTNAME, DATEOFBIRTH, FULLNAME, PLACEOFBIRTH, BIO, IMAGEGALLERY, PUBLICIDIMAGEGALLERY, SOCIALMEDIALINKS, ACTIVE, DATESTARTED, VERIFY, PROFILEIMAGE, BACKGROUDIMAGE, EMAIL)
VALUES 
('mck', '1990-01-01', 'rptmck', 'Place 1', 'Bio 1', 'gallery1.jpg', 'publicgallery1', 'Social Links 1', 'Active', '2023-10-25', 1, 'Avatar/System/807831_rrsd2v.png', 'Avatar/System/807831_rrsd2v.png', 'mck@gmail.com'),
('jvke', '1995-05-05', 'Sigjvke', 'Place 2', 'Bio 2', 'gallery2.jpg', 'publicgallery2', 'Social Links 2', 'Active', '2023-10-26', 1, 'Avatar/System/807831_rrsd2v.png', 'Avatar/System/807831_rrsd2v.png', 'jvke@gmail.com');


INSERT INTO USERTYPE (NAMETYPE, STARTDATE, ENDDATE, STATUS, PAYMENTSTATUS, ACCOUNTID, SUBCRIPTIONID)
VALUES 
('PREMIUM', '2023-10-25', '2023-11-25', 'Active', 1, 'jvke@gmail.com', 1),
('BASIC', '2023-10-26', '2023-11-26', 'Inactive', 0, 'mck@gmail.com', 2),
('PREMIUM', '2023-10-27', '2023-11-27', 'Active', 1, 'khoandps24828@fpt.edu.vn', 3);


INSERT INTO SUBCRIPTIONS (SUBCRIPTIONTYPE, PRICE, DESCRIPTION, CREATEDATE, DURATION)
VALUES 
(1, 9.99, 'Subscription 1', '2023-10-25', 30),
(2, 19.99, 'Subscription 2', '2023-10-26', 90),
(3, 29.99, 'Subscription 3', '2023-10-27', 365);


INSERT INTO FOLLOWER (FOLLOWDATE, ACCOUNT_A, ACCOUNT_B)
VALUES 
('2023-10-25', 1, 2),
('2023-10-26', 1, 3),
('2023-10-27', 2, 3);


INSERT INTO AUTHOR (IDROLE, EMAIL)
VALUES 
(1, 'jvke@gmail.com'),
(2, 'mck@gmail.com'),
(3, 'khoandps24828@fpt.edu.vn');


INSERT INTO ACCOUNTS (EMAIL,PASSWORD,USENAME,BIRTHDAY,GENDER,COUNTRY,ISVERIFY,IMAGEID)
VALUES 
('jvke@gmail.com', '$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G', 'jvkeeee', '2001-03-03', 1, 'US', 1, 'Avatar/System/807831_rrsd2v.png'),
('mck@gmail.com', '$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G', 'prt.mck', '1995-05-05', 2, 'VN', 1, 'Avatar/System/807831_rrsd2v.png'),
('khoandps24828@fpt.edu.vn', '$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G', 'khoa', '2000-10-10', 1, 'CA', 0, 'Avatar/System/807831_rrsd2v.png');


INSERT INTO ROLE (NAMEROLE)
VALUES 
('STAFF'),
('MANAGER'),
('ARTIST'),
('USER'),
('PODCAST');

INSERT INTO  IMAGES 
	VALUES ('Avatar/System/807831_rrsd2v.png',512,'https://res.cloudinary.com/div9ldpou/image/upload/v1696293833/Avatar/System/807831_rrsd2v.png','https://res.cloudinary.com/div9ldpou/image/upload/v1696293833/Avatar/System/807831_rrsd2v.png',512)



INSERT INTO ACCOUNTS(EMAIL,PASSWORD,USENAME,BIRTHDAY,GENDER,COUNTRY,ISVERIFY,IMAGEID) 
	VALUES ('mck@gmail.com','$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G','rpt.mckeyyyyy','2001/03/02',1,'VN',1,'Avatar/System/807831_rrsd2v.png')
	
INSERT INTO ACCOUNTS(EMAIL,PASSWORD,USENAME,BIRTHDAY,GENDER,COUNTRY,ISVERIFY,IMAGEID) 
	VALUES ('jvke@gmail.com','$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G','jvkeeee','2001/03/03',1,'US',1,'Avatar/System/807831_rrsd2v.png')

INSERT INTO ACCOUNTS(EMAIL,PASSWORD,USENAME,BIRTHDAY,GENDER,COUNTRY,ISVERIFY,IMAGEID) 
	VALUES ('nhien@gmail.com','$2a$12$eX7AUZVUW.QC.6ZNxwXtUu0Qn03546/D58VH/oqnN3uhXF1044v.G','nhien','2001/03/03',1,'US',1,'Avatar/System/807831_rrsd2v.png')

Truncate table ARTIST
INSERT INTO ARTIST(ARTISTNAME,FULLNAME,DATEOFBIRTH,PLACEOFBIRTH,BIO,SOCIALMEDIALINKS,VERIFY,EMAIL) 
			VALUES('MCK','Nghiêm Vũ Hoàng Long','2001/03/02', N'Hà Nội, Việt Nam','Hanoi born-and-raised. CDSL // RPT
Nghiêm Vũ Hoàng Long, also known as MCK, is a rapper/singer-songwriter from Hanoi, Vietnam. His music career started in early 2018 as an independent singer/songwriter under the alias Ngơ. His debut single was the smashing hit "Tình Đắng Như Ly Cà Phê" featuring Nân, accumulating over 60 million streams across the DSPs since its upload, kick-starting one of the most successful debuts in the local independent scene.
MCKs career took a turn in 2020 when he appeared as a contestant on the hit TV show "Rap Việt.. He has proven himself to be a force to be reckoned with in Vietnams hip-hop scene as one of the most prominent independent rappers. MCK further solidified his position in the industry by winning the WeChoice Awards for Most Promising Hip-hop Act in 2020.
In recent years, he has become a household name and an unstoppable force on the local chart, especially with the release of his debut album "99%." The album was immediately received with critical acclaim by the media and fans alike, with the single "Chìm Sâu" being the most streamed song on Spotify Việt Nam in 2022, and the rest of the album dominating Billboard Vietnams charts with 5 tracks in the top 10','https://www.instagram.com/rpt.mckeyyyyy/',1,'mck@gmail.com')



INSERT INTO SLIDE VALUES('ARTIST','http://res.cloudinary.com/div9ldpou/image/upload/v1697612547/ImageManager/Image%20News/admin-Dashboard.png.png')

INSERT INTO [ROLE] VALUES('STAFF'),('MANAGER'),('ARTIST'),('USER'),('PODCAST')

INSERT INTO AUTHOR VALUES(4,'jvke@gmail.com')
INSERT INTO AUTHOR VALUES(3,'mck@gmail.com')
INSERT INTO AUTHOR VALUES(4,'nhien@gmail.com')

INSERT INTO SUBCRIPTIONS VALUES(1,123.123,'BASIC usage package', '2023-10-21',1)
INSERT INTO SUBCRIPTIONS VALUES(2,678.678,'PREMIUM usage package', '2023-10-21',1)

INSERT INTO USERTYPE  VALUES ('BASIC','2023-10-21','2024-10-21','OK',1,'jvke@gmail.com',1),
							 ('PREMIUM','2023-10-21','2024-10-21','OK',1,'jvke@gmail.com',1)
INSERT INTO USERTYPE  VALUES ('BASIC','2023-10-21','2024-10-21','OK',2,'mck@gmail.com',1)

							
							
if OBJECT_ID('sp_filter') is not null
	drop proc sp_filter
go 
CREATE PROCEDURE sp_filter
  @month int = NULL,
  @year int = NULL
 
AS
BEGIN
  -- Không có giá trị @year hoặc @month
  IF @year IS NULL AND @month IS NULL
  BEGIN
    SELECT * FROM NEWS
  END
  -- Chỉ có giá trị @year
  ELSE IF @year IS NOT NULL AND @month IS NULL
  BEGIN
    SELECT * FROM NEWS WHERE YEAR(CREATEDATE) = @year
  END
  -- Chỉ có giá trị @month
  ELSE IF @year IS NULL AND @month IS NOT NULL
  BEGIN
    SELECT  * FROM NEWS   inner join IMAGES ON NEWS.[IMAGE] = IMAGES.ACCESSID 
						    inner join ACCOUNTS on ACCOUNTS.EMAIL = NEWS.[AUTHORID]
							where MONTH(CREATEDATE) = @month
  END
  -- Cả hai tham số @year và @month đều có giá trị
  ELSE
  BEGIN
    SELECT * FROM NEWS WHERE YEAR(CREATEDATE) = @year AND MONTH(CREATEDATE) = @month
  END
END

exec sp_filter  12

