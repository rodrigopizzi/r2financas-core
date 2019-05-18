CREATE TABLE "income" (
  "idincome" int PRIMARY KEY,
  "description" varchar NOT NULL,
  "value" double NOT NULL
);

CREATE SEQUENCE seq_income;