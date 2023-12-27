
IF OBJECT_ID('trg_auto_setUserBASIC_And_SetImage') IS NOT NULL
    DROP TRIGGER trg_auto_setUserBASIC_And_SetImage;
GO
CREATE TRIGGER trg_auto_setUserBASIC_And_SetImage
ON accounts
AFTER INSERT, UPDATE
AS
BEGIN
  
	-- auto set usertype -> basic when sign up verified
    INSERT INTO usertype(accountid, startdate, nametype)
    SELECT email, GETDATE(), 'BASIC'
    FROM inserted
    WHERE isblocked = 'false' AND isverify = 'true';

   
   -- auto set image of system when sign up verified (isverify = true and isblocked = false)
    UPDATE accounts
    SET imageid = '5de0710a34648bd783c75eb9d8827247'
    FROM accounts
    INNER JOIN inserted ON accounts.email = inserted.email
    WHERE (inserted.isblocked = 'false' AND inserted.isverify = 'true') OR (inserted.isverify IS NULL);
END;


