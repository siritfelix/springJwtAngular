# Proyecto spring por Felix Sirit

Este proecto contiene una muestra usando una Api Rest en spring para autenticacion por medio de JWT, manejado por medio de Cookie.

- Contine un flujo para registro de usuario que por defecto contendran el Rol USER
- Se usa un iterceptor para hacer un refresh del token en caso de que expire
- El formulario de login contiene validaciones basicas
- Se uso el storage del navegador para guardad datos del perdil del usuario logueado
- Contiene una seccion de Tareas para usuarios con perfil USER
- Contiene una seccion para usuario Administrador, que le permite elimina o actualizar el rol de un usuario

## Flujo de registro y autenticacion en la api Rest

- POST `/api/v1/auth/signup` Registra un usuario
- POST `/api/v1/auth/signin` Login del usuario
- POST `/api/v1/auth/signout` Logout del usuario

## Flujo de gestion de tareas para usuario ROL USER

- GET `/tasks` Obtiene todas las tareas del usuario
- GET `/tasks/filters` Obtiene todas las tareas del usuario, aplicando un filtro por prioridad y tarea completada
- PATCH `/tasks/{id}` Actualiza una tarea
- DELETE `/tasks/{id}` Elimina una tarea
- POST `/tasks` Crea una tarea

## Flujo de gestion de usuarios para usuario ROL ADMIN

- GET `/users` Obtiene todas los usuario
- PATCH `/users/{id}/{role}` Actualiza el rol de un usuario
- DELETE `/users/{id}` Elimina un usuario

## Inicio de la apliacion

RUN `mvn clean install` para instalar todas las dependencias

RUN `mvn spring-boot:run` para buildear el proyecto

## Datos por defecto
Al iniciar se agregaron varios datos a la base de datos embebida H2, para pobrar la aplicacion
se puede hacer login con 
{
    "username": "siritfelix@gmail.com",
    "password": "12345"
}

