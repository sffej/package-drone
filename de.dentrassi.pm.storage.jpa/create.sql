DROP TABLE ARTIFACTS;
DROP TABLE CHANNELS;

CREATE TABLE CHANNELS (
	ID			VARCHAR(36) NOT NULL,
	
	PRIMARY KEY (ID)
);

CREATE TABLE ARTIFACTS (
	ID			VARCHAR(36) NOT NULL,
	CHANNEL		VARCHAR(36) NOT NULL,
	DATA		OID NOT NULL,
	
 	PRIMARY KEY (ID),
 	FOREIGN KEY (CHANNEL) REFERENCES CHANNELS(ID)	
);

CREATE TABLE ARTIFACT_PROPERTIES (
	ART_ID		VARCHAR(36) NOT NULL,
	"KEY"		VARCHAR(255) NOT NULL,
	"VALUE"		TEXT,
	
	PRIMARY KEY (ART_ID, "KEY" )
);

CREATE RULE DROP_ARTIFACTS AS ON DELETE TO ARTIFACTS
	DO SELECT LO_UNLINK ( OLD.DATA );
	
CREATE RULE UPDATE_ARTIFACTS AS ON UPDATE TO ARTIFACTS
	DO SELECT LO_UNLINK ( OLD.DATA )
	WHERE OLD.DATA <> NEW.DATA;
	