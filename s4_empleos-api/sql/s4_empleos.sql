
--insert into EstudianteEntity (id, nombre, idMedioDepago, carrera, correo, calificacionPromedio, horarioDeTrabajo, semestre) values(1, 'David', 1, 'Sistemas', 'algo@uniandes.edu.co', 2, 'a', 4);
insert into ContratistaEntity (id, contrasena, email, esExterno, rutaImagen) values(20, 'Dsdf', 'dsa@hotmail.com', 'true','dsfs');
SELECT * FROM COntratistaEntity
SELECT * FROM CuentaDeCobroEntity;

delete from EstudianteEntity;
delete from TarjetaDeCreditoEntity;
delete from CalificacionEntity;
delete from FacturaEntity;
delete from CUENTADECOBROENTITY;
delete from OFERTAENTITY;
delete from CUENTABANCARIAENTITY;

delete from TokenEntity;
delete from CredencialesEntity;


Select * from TokenEntity;

insert into CuentaDeCobroEntity (numeroCuentaDeCobro,contratista,fecha, valor,nombreEstudiante,concepto)

insert into ContratistaEntity (contrasena,email,esexterno,nombre,rutaimagen) 
values(1,null,null,23,'benito','f');
insert into CuentaDeCobroEntity (numeroCuentaDeCobro,contratista,fecha, valor,nombreEstudiante,concepto)
values(2,'Lol',null,23,'Nicolas','SuperF');
insert into EstudianteEntity (nombre, idMedioDePago, carrera, correo, calificacionPromedio, horarioDeTrabajo, semestre)
values ('David', 0, 'Sistemas', 'awdawd@uniandes.edu.co', 2.0, 'a', 4);
insert into EstudianteEntity (nombre, idMedioDePago, carrera, correo, calificacionPromedio, horarioDeTrabajo, semestre)

values ('David', 0, 'Sistemas', 'awdawd@uniandes.edu.co', 2.0, 'a', 4);

select * from EstudianteEntity;
delete from EstudianteEntity;

values ('Nicolas', 2, 'Sistemas', 'lol@uniandes.edu.co', 5.0, '2:00', 4);

insert into TokenEntity(id,tipo,token)values(40,'Estudiante','Primero');

insert into TokenEntity(ID,TIPO,TOKEN)
values(2,'Contratista','Segundo');


insert into CredencialesEntity (correo, contrasenia, tipo) values ('algo@uniandes.edu.co', 'contrasenia', 'Estudiante');
insert into CredencialesEntity (correo, contrasenia, tipo) values ('algo2@uniandes.edu.co', 'contrasenia', 'Contratista');
select * from CredencialesEntity;