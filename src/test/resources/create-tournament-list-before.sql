delete from tournament_teams;
delete from tournament;
delete from team;

insert into tournament(id, name, status) values
("ed04854e-1332-4eb5-b1fb-7aeedeadadcb", "poedanie radugi", 0),
("8b1a20d7-1462-48eb-9a20-cc3bda04d803", "gladki koteek", 0);

insert into team(id, name, rating) values
("6123906e-8832-4068-9bb1-036f62b90845", "adin", 50.0),
("35725073-36fa-49a3-92b1-5b4d9f024cf1", "dwa", 50.0),
("625cac21-40e8-404c-979a-359639de4f50", "chetire", 50.0),
("7c66680e-87b4-492d-91cb-c95698ddb977", "pyat'", 50.0),
("1e2e68dc-2dbe-4a85-9891-a0de2696581b", "shest'", 50.0),
("b43e7e94-a198-4817-b1c4-def76b7038fb", "sem'", 50.0),
("c590a7f4-670a-40a1-8bd5-496e411d70cd", "vosem'", 50.0),
("2e06a56a-94b7-404f-a987-b149bc20a06d", "tri", 50.0),
("7a85c2fd-9901-4fa4-b640-95e5a34bfd58", "vosem'", 50.0),
("82e36e16-eafe-4eab-8b4e-06cdefe633ed", "devat'", 50.0),
("590f7eee-990a-4621-9654-70ac0e66268d", "desyat'", 50.0),
("889b19da-abc9-4b06-81d0-b9de5927df09", "odinadcat'", 50.0),
("1a54b5cb-5fcf-4775-9114-8df32e79fe7e", "dvenadcat'", 50.0),
("9b1cf336-c20c-4308-98d7-0f104647c7d3", "chtyrnadcat'", 50.0),
("1a626319-e872-4a79-a0d4-ca87cee370e1", "pyatnadcat'", 50.0),
("831841f8-c316-48fe-a6ee-8d3f75d799fe", "shestnadcyat'", 50.0),
("417dd32f-0842-466e-a3e2-55436d7a357d", "semnadcyat'", 50.0);