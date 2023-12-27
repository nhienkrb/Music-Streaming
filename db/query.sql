-- lấy tất cả các playlist songs được tạo theo role manager và staff
select playlists.* from usertype
						join accounts on usertype.accountid = accounts.email
						join author on accounts.email = author.email
						join playlists on playlists.usertypeid = usertype.usertypeid
						join playlist_recording on  playlist_recording.playlistsid = playlists.playlistid
					where author.idrole = 4 or author.idrole = 5
					group by playlists.playlistid, 
						playlists.createdate,
						playlists.description,
						playlists.ispublic,
						playlists.playlistname,
						playlists.quantity,
						playlists.image,
						playlists.usertypeid

-- lấy tất cả các playlist podcast được tạo theo role manager và staff
select playlists.* from usertype
						join accounts on usertype.accountid = accounts.email
						join author on accounts.email = author.email
						join playlists on playlists.usertypeid = usertype.usertypeid
						join playlist_podcast on  playlist_podcast.playlistid = playlists.playlistid
					where author.idrole = 4 or author.idrole = 5
					group by playlists.playlistid, 
						playlists.createdate,
						playlists.description,
						playlists.ispublic,
						playlists.playlistname,
						playlists.quantity,
						playlists.image,
						playlists.usertypeid


select top 10  episodes.* from podcast 
						left join tags on podcast.category = tags.tagid
						left join  episodes on podcast.podcastid = episodes.podcastid
					where tags.nametag in ('Relationships')
					and episodes.ispublic = 1  and episodes.publishdate < GETDATE()
					ORDER BY NEWID()
					
<<<<<<< HEAD
=======
select * from tags where tagid in (2,16)


USE MUSICSTREAMING

create PROCEDURE SEARCH @KEYWORD NVARCHAR(100)
AS
    BEGIN
        SELECT
            A.EMAIL, A.USENAME, IMG.URL,
            ART.ARTISTID, ART.ARTISTNAME, IMGART.URL,
            AB.ALBUMID, AB.ALBUMNAME, IMGAB.URL,											
            S.SONGSID, S.SONGNAME, R.DURATION, IMGS.URL,
            P.PODCASTID, P.PODCASTNAME, IMGP.URL,
            E.EPISODESID, E.EPISODESTITLE, IMGE.URL,
            PL.PLAYLISTID, PL.PLAYLISTNAME, IMGPL.URL,
            R.MOOD,
            GR.ID, GR.NAMEGENRE
        FROM
            ACCOUNTS A
            LEFT JOIN USERTYPE UT ON A.EMAIL = UT.ACCOUNTID
            LEFT JOIN PLAYLISTS PL ON UT.USERTYPEID = PL.PLAYLISTID
            LEFT JOIN PODCAST P ON A.EMAIL = P.ACCOUNTID
            LEFT JOIN EPISODES E ON P.PODCASTID = E.PODCASTID
            LEFT JOIN ARTIST ART ON A.EMAIL = ART.EMAIL
            LEFT JOIN ALBUM AB ON ART.ARTISTID = AB.ARTISTID
            LEFT JOIN TRACK TK ON AB.ALBUMID = TK.ALBUMID
            LEFT JOIN RECORDING R ON TK.RECORDINGID = R.RECORDINGID
            LEFT JOIN SONGS S ON R.SONGSID = S.SONGSID
            LEFT JOIN SONGGENRE SG ON R.RECORDINGID = SG.IDRECORD
			LEFT JOIN GENRE GR ON SG.IDGENRE = GR.ID
			LEFT JOIN IMAGES IMG ON A.IMAGEID = IMG.ACCESSID
			LEFT JOIN IMAGES IMGART ON ART.PROFILEIMAGE = IMGART.ACCESSID
			LEFT JOIN IMAGES IMGAB ON AB.COVERIMAGE = IMGAB.ACCESSID
			LEFT JOIN IMAGES IMGS ON S.IMAGEID = IMGS.ACCESSID
			LEFT JOIN IMAGES IMGP ON P.IMGAGEID = IMGP.ACCESSID
			LEFT JOIN IMAGES IMGE ON E.IMAGEEP = IMGE.ACCESSID
			LEFT JOIN IMAGES IMGPL ON PL.IMAGE = IMGPL.ACCESSID
        WHERE
			S.SONGNAME LIKE N'%' + @KEYWORD + N'%'
			AND S.REALEASEDAY < GETDATE()
			AND S.ISDELETED = 0
			OR (E.EPISODESTITLE LIKE N'%' + @KEYWORD + N'%'
			AND E.ISPUBLIC = 1
			AND E.ISDELETED = 0
			AND E.PUBLISHDATE < GETDATE())
            OR A.USENAME LIKE N'%' + @KEYWORD + N'%'
            OR ART.ARTISTNAME LIKE N'%' + @KEYWORD + N'%'
			OR (AB.ALBUMNAME LIKE N'%' + @KEYWORD + N'%'
			AND AB.RELEASEDATE < GETDATE())
			OR P.PODCASTNAME LIKE N'%' + @KEYWORD + N'%'			
			OR R.MOOD LIKE N'%' + @KEYWORD + N'%'		   
END

EXEC SEARCH @KEYWORD = 't'

GO
-- SEARCH ARTIST
CREATE PROCEDURE SP_SEARCH_ART @ID BIGINT
AS
	BEGIN
		SELECT ART.ARTISTID, ART.ARTISTNAME, IMGART.URL AS 'IMAGEGALLERY', IMGART2.URL AS 'BACKGROUDIMAGE', S.*, IMGS.URL, R.LISTENED, R.DURATION
		FROM ARTIST ART
			LEFT JOIN WRITTER W ON ART.ARTISTID = W.ARTISTID
			LEFT JOIN SONGS S ON W.SONGSID = S.SONGSID
			LEFT JOIN RECORDING R ON S.SONGSID = R.SONGSID
			LEFT JOIN IMAGES IMGART ON ART.IMAGEGALLERY = IMGART.ACCESSID
			LEFT JOIN IMAGES IMGART2 ON ART.BACKGROUDIMAGE = IMGART2.ACCESSID
			LEFT JOIN IMAGES IMGS ON S.IMAGEID = IMGS.ACCESSID
		WHERE ART.ARTISTID = @ID
END

GO
-- SEARCH PLAYLIST
CREATE PROCEDURE SP_SEARCH_PL @ID BIGINT
AS
	BEGIN
		SELECT S.* 
		FROM PLAYLISTS PL
			LEFT JOIN PLAYLIST_RECORDING  PR ON PR.PLAYLISTSID = PL.PLAYLISTID
			LEFT JOIN RECORDING R ON PR.RECORDINGID = R.RECORDINGID
			LEFT JOIN SONGS S ON R.SONGSID = S.SONGSID
		WHERE PL.PLAYLISTID LIKE @ID
END

GO
-- SEARCH ALBUM
CREATE PROCEDURE SP_SEARCH_AL @ID INT
AS
	BEGIN
		SELECT ART.ARTISTID,ART.ARTISTNAME,AL.ALBUMID, AL.ALBUMNAME, IMGAL.URL, R.RECORDINGID, R.RECORDINGIDNAME, R.DURATION, S.*, IMGS.URL
		FROM ALBUM AL
			LEFT JOIN ARTIST ART ON AL.ARTISTID = ART.ARTISTID
			LEFT JOIN TRACK TK ON AL.ALBUMID = TK.ALBUMID
			LEFT JOIN RECORDING R ON TK.RECORDINGID = R.RECORDINGID
			LEFT JOIN SONGS S ON R.SONGSID = S.SONGSID
			LEFT JOIN IMAGES IMGAL ON AL.COVERIMAGE = IMGAL.ACCESSID
			LEFT JOIN IMAGES IMGS ON S.IMAGEID = IMGS.ACCESSID
		WHERE AL.ALBUMID LIKE @ID
END

GO
-- SEARCH ACCOUNT
CREATE PROCEDURE SP_SEARCH_A @EMAIL VARCHAR(255)
AS
	BEGIN
		SELECT PL.*, AUA.EMAIL ,AUA2.EMAIL
		FROM ACCOUNTS A
			LEFT JOIN USERTYPE U ON A.EMAIL = U.ACCOUNTID
			LEFT JOIN PLAYLISTS PL ON U.USERTYPEID = PL.USERTYPEID

			LEFT JOIN AUTHOR AUA ON A.EMAIL = AUA.EMAIL
			LEFT JOIN FOLLOWER FL ON AUA.AUTHORID = FL.ACCOUNT_A
			LEFT JOIN AUTHOR AUA2 ON FL.ACCOUNT_B = AUA2.AUTHORID
		WHERE A.EMAIL LIKE '%'+@EMAIL+'%'
END 

GO
-- SEARCH GENRE
CREATE PROCEDURE SP_SEARCH_GR @MOOD NVARCHAR(55)
AS
	BEGIN
		SELECT R.MOOD, PL.PLAYLISTID AS 'PLAYLISTID', PL.PLAYLISTNAME AS 'PLAYLISTNAME', PL.DESCRIPTION AS 'DESCRIPTION',PL.ISPUBLIC AS 'ISPUBLIC', IMGPL.URL AS 'URL'
		FROM GENRE GR
			INNER JOIN SONGGENRE SG ON SG.IDGENRE = GR.ID
			INNER JOIN RECORDING R ON SG.IDRECORD = R.RECORDINGID
			INNER JOIN PLAYLIST_RECORDING PR ON R.RECORDINGID = PR.RECORDINGID
			INNER JOIN PLAYLISTS PL ON PR.PLAYLISTSID = PL.PLAYLISTID
			LEFT JOIN IMAGES IMGPL ON PL.IMAGE = IMGPL.ACCESSID
		WHERE R.MOOD LIKE N'%'+@MOOD+N'%'
END

-- statistics Record
go
CREATE PROCEDURE sp_getRecordingsCount
    @Mode VARCHAR(10) -- 'Month' or 'Year'
AS
BEGIN
    SET NOCOUNT ON;

    IF @Mode = 'Month'
    BEGIN
        WITH Days AS (
            SELECT 1 AS DayNumber
            UNION SELECT 2
            UNION SELECT 3
            UNION SELECT 4
            UNION SELECT 5
            UNION SELECT 6
            UNION SELECT 7
            UNION SELECT 8
            UNION SELECT 9
            UNION SELECT 10
            UNION SELECT 11
            UNION SELECT 12
            UNION SELECT 13
            UNION SELECT 14
            UNION SELECT 15
            UNION SELECT 16
            UNION SELECT 17
            UNION SELECT 18
            UNION SELECT 19
            UNION SELECT 20
            UNION SELECT 21
            UNION SELECT 22
            UNION SELECT 23
            UNION SELECT 24
            UNION SELECT 25
            UNION SELECT 26
            UNION SELECT 27
            UNION SELECT 28
            UNION SELECT 29
            UNION SELECT 30
            UNION SELECT 31
        )

        SELECT
            d.DayNumber AS Day,
            COUNT(r.recordingId) AS NumberOfRecordings
        FROM
            Days d
        LEFT JOIN
            recording r ON d.DayNumber = DAY(r.recordingdate)
                AND MONTH(r.recordingdate) = MONTH(GETDATE())
                AND YEAR(r.recordingdate) = YEAR(GETDATE())
        GROUP BY
            d.DayNumber
        ORDER BY
            d.DayNumber;
    END
    ELSE IF @Mode = 'Year'
    BEGIN
        WITH Months AS (
            SELECT 1 AS MonthNumber
            UNION SELECT 2
            UNION SELECT 3
            UNION SELECT 4
            UNION SELECT 5
            UNION SELECT 6
            UNION SELECT 7
            UNION SELECT 8
            UNION SELECT 9
            UNION SELECT 10
            UNION SELECT 11
            UNION SELECT 12
        )

        SELECT
            m.MonthNumber AS Month,
            COUNT(r.recordingId) AS NumberOfRecordings
        FROM
            Months m
        LEFT JOIN
            recording r ON m.MonthNumber = MONTH(r.recordingdate)
                AND YEAR(r.recordingdate) = YEAR(GETDATE())
        GROUP BY
            m.MonthNumber
        ORDER BY
            m.MonthNumber;
    END

END

exec sp_getRecordingsCount 'month'

CREATE PROCEDURE sp_getEpisodesCount
    @Mode VARCHAR(10) -- 'Month' or 'Year'
AS
BEGIN
    SET NOCOUNT ON;

    IF @Mode = 'Month'
    BEGIN
        WITH Days AS (
            SELECT 1 AS DayNumber
            UNION SELECT 2
            UNION SELECT 3
            UNION SELECT 4
            UNION SELECT 5
            UNION SELECT 6
            UNION SELECT 7
            UNION SELECT 8
            UNION SELECT 9
            UNION SELECT 10
            UNION SELECT 11
            UNION SELECT 12
            UNION SELECT 13
            UNION SELECT 14
            UNION SELECT 15
            UNION SELECT 16
            UNION SELECT 17
            UNION SELECT 18
            UNION SELECT 19
            UNION SELECT 20
            UNION SELECT 21
            UNION SELECT 22
            UNION SELECT 23
            UNION SELECT 24
            UNION SELECT 25
            UNION SELECT 26
            UNION SELECT 27
            UNION SELECT 28
            UNION SELECT 29
            UNION SELECT 30
            UNION SELECT 31
        )

        SELECT
            d.DayNumber AS Day,
            COUNT(r.publishdate) AS NumberOfRecordings
        FROM
            Days d
        LEFT JOIN
            episodes r ON d.DayNumber = DAY(r.publishdate)
                AND MONTH(r.publishdate) = MONTH(GETDATE())
                AND YEAR(r.publishdate) = YEAR(GETDATE())
        GROUP BY
            d.DayNumber
        ORDER BY
            d.DayNumber;
    END
    ELSE IF @Mode = 'Year'
    BEGIN
        WITH Months AS (
            SELECT 1 AS MonthNumber
            UNION SELECT 2
            UNION SELECT 3
            UNION SELECT 4
            UNION SELECT 5
            UNION SELECT 6
            UNION SELECT 7
            UNION SELECT 8
            UNION SELECT 9
            UNION SELECT 10
            UNION SELECT 11
            UNION SELECT 12
        )

        SELECT
            m.MonthNumber AS Month,
            COUNT(r.episodesid) AS NumberOfRecordings
        FROM
            Months m
        LEFT JOIN
            episodes r ON m.MonthNumber = MONTH(r.publishdate)
                AND YEAR(r.publishdate) = YEAR(GETDATE())
        GROUP BY
            m.MonthNumber
        ORDER BY
            m.MonthNumber;
    END

END

exec sp_getEpisodesCount 'year'

