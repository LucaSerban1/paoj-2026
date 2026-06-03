DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS servicii_bancare;
DROP TABLE IF EXISTS carduri;
DROP TABLE IF EXISTS conturi;
DROP TABLE IF EXISTS clienti;

CREATE TABLE clienti (
    cnp   VARCHAR(13)  PRIMARY KEY,
    nume  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE conturi (
    iban              VARCHAR(24)      PRIMARY KEY,
    cnp_client        VARCHAR(13)      NOT NULL,
    tip_cont          VARCHAR(20)      NOT NULL,
    sold              DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    data_deschidere   DATE             NOT NULL,
    limita_descoperit DOUBLE PRECISION DEFAULT NULL,
    comision_lunar    DOUBLE PRECISION DEFAULT NULL,
    rata_dobanda      DOUBLE PRECISION DEFAULT NULL,
    perioada_minima   INT              DEFAULT NULL,
    FOREIGN KEY (cnp_client) REFERENCES clienti(cnp) ON DELETE CASCADE
);

CREATE TABLE carduri (
    numar_card     VARCHAR(16)      PRIMARY KEY,
    iban           VARCHAR(24)      NOT NULL,
    cnp_client     VARCHAR(13)      NOT NULL,
    tip_card       VARCHAR(20)      NOT NULL,
    data_expirare  DATE             NOT NULL,
    cvv            VARCHAR(3)       NOT NULL,
    activ          BOOLEAN          NOT NULL DEFAULT TRUE,
    limita_zilnica DOUBLE PRECISION DEFAULT NULL,
    contactless    BOOLEAN          DEFAULT NULL,
    limita_credit  DOUBLE PRECISION DEFAULT NULL,
    sold_utilizat  DOUBLE PRECISION DEFAULT NULL,
    FOREIGN KEY (iban)       REFERENCES conturi(iban)  ON DELETE CASCADE,
    FOREIGN KEY (cnp_client) REFERENCES clienti(cnp)   ON DELETE CASCADE
);

CREATE TABLE tranzactii (
    id              VARCHAR(50)      PRIMARY KEY,
    iban            VARCHAR(24)      NOT NULL,
    suma            DOUBLE PRECISION NOT NULL,
    tip_tranzactie  VARCHAR(20)      NOT NULL,
    data_tranzactie TIMESTAMP        NOT NULL,
    descriere       VARCHAR(255),
    FOREIGN KEY (iban) REFERENCES conturi(iban) ON DELETE CASCADE
);

CREATE TABLE servicii_bancare (
    id            SERIAL           PRIMARY KEY,
    iban          VARCHAR(24)      NOT NULL,
    tip_serviciu  VARCHAR(50)      NOT NULL,
    cost_lunar    DOUBLE PRECISION NOT NULL,
    durata_luni   INT              NOT NULL,
    data_activare DATE             NOT NULL,
    FOREIGN KEY (iban) REFERENCES conturi(iban) ON DELETE CASCADE
);