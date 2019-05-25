CREATE TABLE "user" (
  "iduser" int PRIMARY KEY,
  "email" varchar UNIQUE NOT NULL,
  "password" varchar NOT NULL
);

CREATE SEQUENCE seq_user;

CREATE TABLE "income" (
  "idincome" int PRIMARY KEY,
  "description" varchar NOT NULL,
  "value" double NOT NULL
);

CREATE SEQUENCE seq_income;