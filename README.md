# Sistema Digital de Gestión de Pedidos y Evaluación de Servicio para Restaurantes con QR

## CS 2031 Desarrollo Basado en Plataforma

**Integrantes:**
- [Nombre 1]
- [Nombre 2]
- [Nombre 3]

## Índice
- [Introducción](#introducción)
- [Identificación del Problema o Necesidad](#identificación-del-problema-o-necesidad)
- [Descripción de la Solución](#descripción-de-la-solución)
- [Modelo de Entidades](#modelo-de-entidades)
- [Testing y Manejo de Errores](#testing-y-manejo-de-errores)
- [Medidas de Seguridad Implementadas](#medidas-de-seguridad-implementadas)
- [Eventos y Asincronía](#eventos-y-asincronía)
- [GitHub](#github)
- [Conclusión](#conclusión)
- [Apéndices](#apéndices)

## Introducción

### Contexto
La experiencia en restaurantes está evolucionando con la digitalización de los procesos. Este proyecto responde a la necesidad de mejorar la eficiencia en la toma de pedidos y la interacción entre los clientes y los meseros. Aunque el mesero sigue desempeñando un papel crucial, el sistema busca facilitar el acceso al menú y optimizar el proceso de pedidos. Además, se incluye un método automatizado de evaluación del servicio mediante códigos QR.

### Objetivos del Proyecto
- Facilitar a los clientes la visualización del menú y la realización de pedidos mediante el escaneo de un código QR.
- Proporcionar una vía para que los clientes califiquen el servicio de los meseros mediante un segundo código QR en la boleta.
- Otorgar al administrador herramientas para la gestión del desempeño de los meseros, incentivando buenas prácticas basadas en las evaluaciones de los clientes.

## Identificación del Problema o Necesidad

### Descripción del Problema
La toma de pedidos en restaurantes suele ser un proceso manual y propenso a errores, lo que puede generar demoras y confusiones. A su vez, la retroalimentación sobre la calidad del servicio es limitada y, muchas veces, imprecisa.

### Justificación
Es necesario mejorar la eficiencia del proceso de pedidos sin eliminar la interacción con los meseros, y permitir una evaluación precisa del servicio para mejorar la experiencia del cliente y la gestión del personal.

## Descripción de la Solución

### Funcionalidades Implementadas
1. **QR en la mesa para acceso al menú y pedidos**: Los clientes pueden escanear un código QR para acceder al menú y realizar pedidos de forma digital.
2. **Carrito de compras virtual**: Los clientes pueden agregar productos al carrito, modificarlos y personalizar sus pedidos.
3. **QR personalizado para meseros**: Un código QR específico para cada mesero se incluye en la boleta, lo que permite una evaluación rápida y directa.
4. **Sistema de evaluación del servicio**: Los clientes pueden calificar el servicio del mesero mediante un sistema de evaluación con estrellas.
5. **Panel de control para el administrador**: El administrador puede gestionar las evaluaciones y calificaciones de los meseros.

### Tecnologías Utilizadas
- **Lenguajes de programación**: JavaScript (Node.js para el backend), HTML, CSS.
- **Frameworks**: Express.js para la creación de API, Bootstrap para el frontend.
- **Bases de datos**: MySQL para almacenamiento de datos.
- **API externas**: Google Maps API para la localización de clientes en pedidos a domicilio.

## Modelo de Entidades

### Diagrama de Entidades
![Diagrama de Entidades](./path_to_diagram.png)

### Descripción de Entidades
- **User**: Usuario abstracto que puede ser cliente o empleado.
- **Client**: Cliente que realiza pedidos y deja evaluaciones.
- **Employee**: Mesero que recibe calificaciones de los clientes.
- **Order**: Representa un pedido realizado por un cliente.
- **OrderItem**: Producto individual dentro de un pedido.
- **Product**: Platos o bebidas del menú.
- **Rating**: Evaluaciones realizadas por los clientes sobre el servicio recibido.

## Testing y Manejo de Errores

### Niveles de Testing Realizados
- **Pruebas Unitarias**: Para verificar la funcionalidad de cada componente del sistema.
- **Pruebas de Integración**: Para asegurar que los módulos interactúan correctamente.
- **Pruebas de Sistema**: Validación de que el sistema completo cumple con los requisitos.

### Resultados
Se detectaron y corrigieron errores en la integración del carrito de compras y el panel de control del administrador. El manejo de excepciones garantiza una correcta experiencia de usuario.

### Manejo de Errores
El sistema utiliza un manejo global de excepciones para capturar errores inesperados, con mensajes claros para los usuarios y registros detallados para los administradores.

## Medidas de Seguridad Implementadas

### Seguridad de Datos
- **Cifrado de contraseñas**: Se utiliza bcrypt para el hash de contraseñas.
- **Autenticación**: Implementación de JWT (JSON Web Tokens) para autenticar usuarios.
- **Autorización**: Se implementan permisos basados en roles para el acceso a funcionalidades administrativas.

### Prevención de Vulnerabilidades
- **Protección contra inyección SQL**: Uso de consultas preparadas para evitar inyecciones.
- **Mitigación de XSS y CSRF**: Uso de tokens CSRF y sanitización de entradas de usuario.

## Eventos y Asincronía

El sistema usa eventos asincrónicos para el procesamiento de pedidos en segundo plano, asegurando que la experiencia del usuario no se vea interrumpida mientras el sistema procesa información como la actualización de estado de los pedidos.

## GitHub

### GitHub Projects
Se usaron "issues" para asignar tareas, con fechas límite específicas para cada una. El progreso del proyecto fue gestionado mediante el tablero de GitHub Projects.

### GitHub Actions
Se configuró un flujo de CI/CD (Integración Continua/Entrega Continua) con GitHub Actions para ejecutar pruebas automáticas y despliegues a un servidor de prueba.

## Conclusión

### Logros del Proyecto
El sistema permite a los clientes realizar pedidos de manera digital y evaluar el servicio, proporcionando una herramienta útil tanto para los clientes como para los administradores de los restaurantes.

### Aprendizajes Clave
- Profundización en la integración de sistemas de evaluación.
- Mejora de la experiencia en la implementación de flujos CI/CD.

### Trabajo Futuro
Se podría implementar un sistema de notificaciones en tiempo real para los meseros y optimizar el proceso de evaluación con encuestas más detalladas.

## Apéndices

### Licencia
Este proyecto está bajo la licencia MIT.

### Referencias
- Documentación de Node.js: https://nodejs.org/
- Documentación de MySQL: https://dev.mysql.com/doc/
