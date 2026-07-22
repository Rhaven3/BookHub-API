 

-- predefined type, no DDL - MDSYS.SDO_GEOMETRY 

-- predefined type, no DDL - XMLTYPE 

 

CREATE TABLE Adresse  

    (  

     idAdresse  INTEGER  NOT NULL ,  

     rue        CHAR (50)  NOT NULL ,  

     ville      CHAR (50)  NOT NULL ,  

     codePostal CHAR (20)  NOT NULL ,  

     pays       CHAR (20)  NOT NULL  

    )  

; 

 

ALTER TABLE Adresse  

    ADD CONSTRAINT Adresse_PK PRIMARY KEY ( idAdresse ) ; 

 

CREATE TABLE Auteur  

    (  

     idAuteur INTEGER  NOT NULL ,  

     nom      CHAR (20)  NOT NULL ,  

     prenom   CHAR (50)  NOT NULL  

    )  

; 

 

ALTER TABLE Auteur  

    ADD CONSTRAINT Auteur_PK PRIMARY KEY ( idAuteur ) ; 

 

CREATE TABLE Avis  

    (  

     idAvis        INTEGER  NOT NULL ,  

     note          INTEGER  NOT NULL ,  

     commentaire   CHAR (500) ,  

     "date"        DATE  NOT NULL ,  

     Livre_idLivre INTEGER  NOT NULL ,  

     User_idUser   INTEGER  NOT NULL ,  

     statut        CHAR (20)  NOT NULL  

    )  

; 

 

ALTER TABLE Avis  

    ADD CONSTRAINT Avis_PK PRIMARY KEY ( idAvis ) ; 

 

-- CORRECTION : un avis unique par (livre, utilisateur), et non un seul avis par livre tous users confondus 

CREATE UNIQUE INDEX Avis_Livre_User_UN_IDX ON Avis  

    (  

     Livre_idLivre ASC, User_idUser ASC  

    )  

; 

 

CREATE TABLE Categorie  

    (  

     idCategorie INTEGER  NOT NULL ,  

     nom         CHAR (20)  NOT NULL  

    )  

; 

 

ALTER TABLE Categorie  

    ADD CONSTRAINT Categorie_PK PRIMARY KEY ( idCategorie ) ; 

 

ALTER TABLE Categorie  

    ADD CONSTRAINT Categorie_nom_UN UNIQUE ( nom ) ; 

 

CREATE TABLE Editeur  

    (  

     idEditeur INTEGER  NOT NULL ,  

     nom       CHAR (50)  NOT NULL  

    )  

; 

 

ALTER TABLE Editeur  

    ADD CONSTRAINT Editeur_PK PRIMARY KEY ( idEditeur ) ; 

 

ALTER TABLE Editeur  

    ADD CONSTRAINT Editeur_nom_UN UNIQUE ( nom ) ; 

 

-- CORRECTION : colonne Exemplaire_idExemplaire supprimée (table Exemplaire inexistante dans le modèle) 

CREATE TABLE Emprunt  

    (  

     idEmprunt           INTEGER  NOT NULL ,  

     dateEmprunt         DATE  NOT NULL ,  

     dateRetourPrevue    DATE  NOT NULL ,  

     dateRetourEffective DATE ,  

     statut              CHAR (20)  NOT NULL ,  

     User_idUser         INTEGER  NOT NULL ,  

     Livre_idLivre       INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Emprunt  

    ADD CONSTRAINT Emprunt_PK PRIMARY KEY ( idEmprunt ) ; 

 

CREATE TABLE Image  

    (  

     idImage       INTEGER  NOT NULL ,  

     url           CHAR (255)  NOT NULL ,  

     nom           CHAR (20)  NOT NULL ,  

     Livre_idLivre INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Image  

    ADD CONSTRAINT Image_PK PRIMARY KEY ( idImage ) ; 

 

ALTER TABLE Image  

    ADD CONSTRAINT Image_url_UN UNIQUE ( url ) ; 

 

CREATE TABLE Livre  

    (  

     idLivre           INTEGER  NOT NULL ,  

     titre             CHAR (255)  NOT NULL ,  

     description       CHAR (500) ,  

     anneePublication  INTEGER  NOT NULL ,  

     langue            CHAR (20)  NOT NULL ,  

     Editeur_idEditeur INTEGER  NOT NULL ,  

     isbn              INTEGER  NOT NULL ,  

     disponibilite     CHAR (1)  

    )  

; 

 

ALTER TABLE Livre  

    ADD CONSTRAINT Livre_PK PRIMARY KEY ( idLivre ) ; 

 

-- CORRECTION : isbn doit être unique (deux livres ne partagent jamais le même ISBN) 

ALTER TABLE Livre  

    ADD CONSTRAINT Livre_isbn_UN UNIQUE ( isbn ) ; 

 

CREATE TABLE Notification  

    (  

     idNotification INTEGER  NOT NULL ,  

     type           CHAR (20)  NOT NULL ,  

     message        CHAR (20)  NOT NULL ,  

     "date"         DATE  NOT NULL ,  

     User_idUser    INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Notification  

    ADD CONSTRAINT Notification_PK PRIMARY KEY ( idNotification ) ; 

 

CREATE TABLE Relation_2  

    (  

     Auteur_idAuteur INTEGER  NOT NULL ,  

     Livre_idLivre   INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Relation_2  

    ADD CONSTRAINT Relation_2_PK PRIMARY KEY ( Auteur_idAuteur, Livre_idLivre ) ; 

 

CREATE TABLE Relation_4  

    (  

     Livre_idLivre         INTEGER  NOT NULL ,  

     Categorie_idCategorie INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Relation_4  

    ADD CONSTRAINT Relation_4_PK PRIMARY KEY ( Livre_idLivre, Categorie_idCategorie ) ; 

 

CREATE TABLE Reservation  

    (  

     idReservation         INTEGER  NOT NULL ,  

     dateInscription       DATE  NOT NULL ,  

     dateLimiteReservation DATE  NOT NULL ,  

     statut                CHAR (20)  NOT NULL ,  

     Livre_idLivre         INTEGER  NOT NULL ,  

     User_idUser           INTEGER  NOT NULL  

    )  

; 

 

ALTER TABLE Reservation  

    ADD CONSTRAINT Reservation_PK PRIMARY KEY ( idReservation ) ; 

 

-- CORRECTION : tailles de CHAR précisées pour supprimer les warnings "CHAR size not specified" 

CREATE TABLE "User"  

    (  

     idUser            INTEGER  NOT NULL ,  

     nom               CHAR (50)  NOT NULL ,  

     prenom            CHAR (50)  NOT NULL ,  

     email             CHAR (100)  NOT NULL ,  

     password          CHAR (255)  NOT NULL ,  

     role              CHAR (20)  NOT NULL ,  

     Adresse_idAdresse INTEGER  NOT NULL ,  

     telephone         INTEGER  

    )  

; 

 

ALTER TABLE "User"  

    ADD CONSTRAINT User_PK PRIMARY KEY ( idUser ) ; 

 

ALTER TABLE "User"  

    ADD CONSTRAINT User_email_UN UNIQUE ( email ) ; 

 

-- ============================================================ 

-- CLES ETRANGERES 

-- ============================================================ 

 

ALTER TABLE Avis  

    ADD CONSTRAINT Avis_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Avis  

    ADD CONSTRAINT Avis_User_FK FOREIGN KEY  

    (  

     User_idUser 

    )  

    REFERENCES "User"  

    (  

     idUser 

    )  

; 

 

ALTER TABLE Emprunt  

    ADD CONSTRAINT Emprunt_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Emprunt  

    ADD CONSTRAINT Emprunt_User_FK FOREIGN KEY  

    (  

     User_idUser 

    )  

    REFERENCES "User"  

    (  

     idUser 

    )  

; 

 

ALTER TABLE Image  

    ADD CONSTRAINT Image_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Livre  

    ADD CONSTRAINT Livre_Editeur_FK FOREIGN KEY  

    (  

     Editeur_idEditeur 

    )  

    REFERENCES Editeur  

    (  

     idEditeur 

    )  

; 

 

ALTER TABLE Notification  

    ADD CONSTRAINT Notification_User_FK FOREIGN KEY  

    (  

     User_idUser 

    )  

    REFERENCES "User"  

    (  

     idUser 

    )  

; 

 

ALTER TABLE Relation_2  

    ADD CONSTRAINT Relation_2_Auteur_FK FOREIGN KEY  

    (  

     Auteur_idAuteur 

    )  

    REFERENCES Auteur  

    (  

     idAuteur 

    )  

; 

 

ALTER TABLE Relation_2  

    ADD CONSTRAINT Relation_2_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Relation_4  

    ADD CONSTRAINT Relation_4_Categorie_FK FOREIGN KEY  

    (  

     Categorie_idCategorie 

    )  

    REFERENCES Categorie  

    (  

     idCategorie 

    )  

; 

 

ALTER TABLE Relation_4  

    ADD CONSTRAINT Relation_4_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Reservation  

    ADD CONSTRAINT Reservation_Livre_FK FOREIGN KEY  

    (  

     Livre_idLivre 

    )  

    REFERENCES Livre  

    (  

     idLivre 

    )  

; 

 

ALTER TABLE Reservation  

    ADD CONSTRAINT Reservation_User_FK FOREIGN KEY  

    (  

     User_idUser 

    )  

    REFERENCES "User"  

    (  

     idUser 

    )  

; 

 

ALTER TABLE "User"  

    ADD CONSTRAINT User_Adresse_FK FOREIGN KEY  

    (  

     Adresse_idAdresse 

    )  

    REFERENCES Adresse  

    (  

     idAdresse 

    )  

; 

 

-- ============================================================ 

-- INDEX complémentaires 

-- (Oracle n'indexe pas automatiquement les clés étrangères) 

-- ============================================================ 

 

-- Index demandés explicitement sur Livre 

CREATE INDEX Livre_titre_IDX ON Livre ( titre ) ; 

CREATE INDEX Livre_anneePublication_IDX ON Livre ( anneePublication ) ; 

CREATE INDEX Livre_isbn_IDX ON Livre ( isbn ) ; 

 

-- Autres champs intéressants sur Livre 

CREATE INDEX Livre_langue_IDX ON Livre ( langue ) ; 

CREATE INDEX Livre_disponibilite_IDX ON Livre ( disponibilite ) ; 

CREATE INDEX Livre_Editeur_idEditeur_IDX ON Livre ( Editeur_idEditeur ) ; 

CREATE INDEX Livre_titre_annee_IDX ON Livre ( titre, anneePublication ) ; 

 

-- Index sur les FK des tables de jonction / associatives 

CREATE INDEX Emprunt_Livre_idLivre_IDX ON Emprunt ( Livre_idLivre ) ; 

CREATE INDEX Emprunt_User_idUser_IDX ON Emprunt ( User_idUser ) ; 

CREATE INDEX Reservation_Livre_idLivre_IDX ON Reservation ( Livre_idLivre ) ; 

CREATE INDEX Reservation_User_idUser_IDX ON Reservation ( User_idUser ) ; 

CREATE INDEX Notification_User_idUser_IDX ON Notification ( User_idUser ) ; 

CREATE INDEX User_Adresse_idAdresse_IDX ON "User" ( Adresse_idAdresse ) ; 

CREATE INDEX Image_Livre_idLivre_IDX ON Image ( Livre_idLivre ) ; 

CREATE INDEX Relation_2_Livre_idLivre_IDX ON Relation_2 ( Livre_idLivre ) ; 

CREATE INDEX Relation_4_Categorie_idCategorie_IDX ON Relation_4 ( Categorie_idCategorie ) ; 