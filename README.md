# Sistema Digital de Gestión de Pedidos y Evaluación de Servicio para Restaurantes con QR

## CS 2031 Desarrollo Basado en Plataforma

**Integrantes:**
- Aguinaga Pizarro, Piero Alessandro
- Muñoz Paucar, Fernando Jose
- Tabraj Morales, Sebastián
- Tinco Aliaga, César Abelardo

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
La experiencia en restaurantes está evolucionando con la digitalización de los procesos. Este proyecto responde a la necesidad de mejorar la eficiencia en la toma de pedidos y la interacción entre los clientes y los meseros. Aunque el mesero sigue desempeñando un papel crucial, desde recibir a los clientes hasta entregar los pedidos y resolver dudas, el sistema busca facilitar el acceso al menú y optimizar el proceso de pedidos. Además, se incluye un método automatizado de evaluación del servicio mediante códigos QR, lo que permite a los administradores obtener retroalimentación precisa para mejorar la atención al cliente y premiar el buen desempeño de los meseros.

### Objetivos del Proyecto
- Facilitar a los clientes la visualización del menú y la realización de pedidos mediante el escaneo de un código QR en la mesa, sin reemplazar el rol del mesero, quien continuará recepcionando a los clientes, entregando los pedidos y atendiéndolos en sus necesidades adicionales.
- Proporcionar una vía para que los clientes califiquen el servicio de los meseros al finalizar su consumo, mediante un segundo código QR que estará en la boleta, permitiendo a los administradores obtener comentarios y calificaciones de manera rápida y efectiva.
- Otorgar al administrador herramientas para la gestión del desempeño de los meseros, basadas en los comentarios y calificaciones de los clientes, para incentivar buenas prácticas mediante bonificaciones o menciones especiales.

## Identificación del Problema o Necesidad

### Descripción del Problema
El sistema busca resolver la necesidad de eficiencia y precisión en la toma de pedidos, a la vez que mantiene el contacto personal entre el mesero y el cliente. La intervención del mesero sigue siendo clave, ya que es quien recibe a los clientes, ofrece la atención durante su estancia y entrega los pedidos. Al mismo tiempo, la implementación de un sistema de evaluación mediante códigos QR permite al administrador recopilar comentarios precisos sobre el desempeño de cada mesero, incentivando la mejora continua y asegurando una experiencia de calidad para los clientes.

### Justificación
Es relevante solucionar este problema porque permite agilizar el proceso de pedidos, reduciendo errores y tiempos de espera, lo que incrementa la satisfacción del cliente. Además, el sistema de evaluación fomenta una cultura de mejora continua en los empleados, lo que beneficia tanto al restaurante como a los meseros.

## Descripción de la Solución

### Actores del Negocio
1. **Manager**: Encargo de gestión de personal, atención al cliente, gestión de inventarios y manejo de caja. 
2. **Mesero**:  Encargado de la atención personal al cliente en el local. 
3. **Repartidor**: Encargado de la entrega de pedidos por delivery. 
4. **Cliente registrado**: Cliente con una cuenta creada en la aplicación del restaurante. 
5. **Cliente general** : Cliente de "al paso" que no está registrado en la aplicación del restaurante. 
6. **Chef** : Recibe ordenes y las prepara.   


### Entidades del Negocio
1. **Usuario**
2. **Manager**
3. **Cliente registrado**
4. **Repartidor**
5. **Mesero**
6. **Mesa** 
7. **Orden**
8. **Reserva**
9. **Delivery**
10. **PedidoLocal**
11. **Producto**
12. **Combos**
13. **ReseñaMozo**
14. **ReseñaDelivery**


### Casos de uso del Negocio
1. **QR en la mesa para acceso al menú y pedidos**: Los clientes pueden escanear un código QR para acceder al menú y realizar pedidos de forma digital.
2. **Carrito de compras virtual**: Los clientes pueden agregar productos al carrito, modificarlos y personalizar sus pedidos.
3. **QR personalizado para meseros**: Un código QR específico para cada mesero se incluye en la boleta, lo que permite una evaluación rápida y directa.
4. **Sistema de evaluación del servicio**: Los clientes pueden calificar el servicio del mesero mediante un sistema de evaluación con estrellas.
5. **Panel de control para el administrador**: El administrador puede gestionar las evaluaciones y calificaciones de los meseros.



### Tecnologías Utilizadas
- **Lenguajes de programación**: Java
- **Frameworks**: Express.js para la creación de API, Bootstrap para el frontend.
- **Bases de datos**: MySQL para almacenamiento de datos.
- **API externas**: Google Maps API para la localización de clientes en pedidos a domicilio.

## Modelo de Entidades

### Diagrama de Entidades
![Diagrama de Entidades](./path_to_diagram.png)

### Descripción de Entidades
#### 1. User (Abstracta)
Representa cualquier tipo de usuario del sistema (cliente o empleado).

**Atributos**:
- `id`: Identificador único.
- `name`: Nombre.
- `email`: Correo electrónico.
- `password`: Contraseña para autenticación.
- `role`: Rol del usuario (ej: cliente, mesero, administrador).
- 
**Métodos de clase**:
- `String getUsername()`: Extrae el email con el que se registró el usuario.
- `boolean isAccountNonExpired()`: Verifica si la cuenta no está vencida.
- `boolean isAccountNonLocked()`: Verifica si la cuenta no está baneada.
- `boolean isCredentialsNonExpired()`: Verifica si la credencial no está vencida.
- `boolean isEnabled()`: Verifica si la cuenta está habilitada.


---

#### 2. Client (Hereda de User)
Representa a los clientes del restaurante.

**Atributos adicionales**:
- `loyaltyPoints`: Puntos de lealtad acumulados por el cliente.
- `preferences`: Preferencias del cliente (ej: sin gluten, vegetariano).
- `orderHistory`: Historial de pedidos realizados.


**Métodos del endpoint "/cliente"**:
- `GET(/{id}) getCliente(id)`: (roles permitidos: ADMIN) Devuelve el clienteResponseDTO de la id, exepciones: ClienteNotFound.
- `GET() getAllClientes()`: (roles permitidos: ADMIN) Devuelve el clienteResponseDTO de todos los clientes.
- `POST() createCliente(clienteRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo cliente, exepciones: ClienteAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteCliente(id)`: (roles permitidos: ADMIN) Elimina un cliente con la id, exepciones:  ClienteNotFound.
- `PATCH(/{id}) updateCliente(PatchClienteDTO)`: (roles permitidos: ADMIN) Actualiza un cliente con la id, exepciones:  ClienteNotFound, IllegalArgumentException.
- `GET({/me}) getCliente()`: (roles permitidos: CLIENTE) Devuelve el clienteResponseDTO del usuario autenticado, exepciones: ClienteNotFound.
- `DELETE(/me) deleteCliente()`: (roles permitidos: CLIENTE) Elimina el cliente autenticado, exepciones:  ClienteNotFound.
- `PATCH(/me) updateCliente(PatchClienteDTO)`: (roles permitidos: CLIENTE) Actualiza un cliente autenticado, exepciones:  ClienteNotFound, IllegalArgumentException.
- - `GET(/me/pedido) getPedidoCliente()`: (roles permitidos: CLIENTE) Devuelve el pedidoResponseDTO del pedido actual(pedido más reciente del historial) del usuario autenticado, exepciones: ClienteNotFound.


---

#### 3. Employee (Hereda de User)
Representa a los empleados, principalmente meseros.

**Atributos adicionales**:
- `position`: Cargo del empleado (ej: mesero).
- `ratings`: Evaluaciones recibidas de los clientes.
- `performanceScore`: Puntaje de desempeño basado en evaluaciones.

**Métodos del endpoint "/employee"**:
- `GET(/{id}) getEmployee(id)`: (roles permitidos: ADMIN) Devuelve el employeeResponseDTO de la id, exepciones: EmployeeNotFound.
- `GET() getAllEmployees()`: (roles permitidos: ADMIN) Devuelve el employeeResponseDTO de todos los clientes.
- `POST() createEmployee(employeeRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo employee, exepciones: EmployeeAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteEmployee(id)`: (roles permitidos: ADMIN) Elimina un employee con la id, exepciones:  EmployeeNotFound.
- `PATCH(/{id}) updateEmployee(PatchEmployeeDTO)`: (roles permitidos: ADMIN) Actualiza un cliente con la id, exepciones:  EmployeeNotFound, IllegalArgumentException.
- `GET({/me}) getEmployee()`: (roles permitidos: EMPLOYEE) Devuelve el employeeResponseDTO del usuario autenticado, exepciones: EmployeeNotFound.
- `DELETE(/me) deleteEmployee()`: (roles permitidos: EMPLOYEE) Elimina el employee autenticado, exepciones:  EmployeeNotFound.
- `PATCH(/me) updateEmployee(PatchEmployeeDTO)`: (roles permitidos: EMPLOYEE) Actualiza un cliente autenticado, exepciones:  EmployeeNotFound, IllegalArgumentException.


---

#### 4. Table
Representa las mesas del restaurante.

**Atributos**:
- `id`: Identificador único de la mesa.
- `qrCode`: Código QR asociado a la mesa (para escanear y acceder al menú).
- `location`: Ubicación de la mesa dentro del restaurante.
- `capacity`: Capacidad de personas que pueden sentarse en la mesa.
- `isAvailable`: Indica si la mesa está disponible.

**Métodos del endpoint "/table"**:
- `GET(/{id}) getTable(id)`: (roles permitidos: ADMIN) Devuelve el tableResponseDTO de la id, exepciones: TableNotFound.
- `GET() getAllTable()`: (roles permitidos: ADMIN) Devuelve el tableResponseDTO de todas las mesas.
- `POST() createTable(tableRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo table, exepciones: TableAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteTable(id)`: (roles permitidos: ADMIN) Elimina un employee con la id, exepciones:  TableNotFound.
- `PATCH(/{id}) updateTable(PatchTableDTO)`: (roles permitidos: ADMIN) Actualiza un cliente con la id, exepciones:  TableNotFound, IllegalArgumentException.
- `PATCH(/changestatus/{id}) changeTableStatus()`: (roles permitidos: EMPLOYEE) Commnuta el estado de "isAvailable" de la mesa, exepciones:  TableNotFound.
-  `GET(/availableTables) getAvalilableTables()`: (roles permitidos: ADMIN) Devuelve el número de mesa de todas las mesas disponibles en ese momento.

---

#### 5. Order
Representa los pedidos realizados por los clientes.

**Atributos**:
- `id`: Identificador único del pedido.
- `client`: Cliente que realizó el pedido (relación con Client).
- `orderDate`: Fecha en la que se realizó el pedido.
- `orderTime`: Hora en la que se realizó el pedido.
- `orderItems`: Lista de ítems del menú pedidos (relación con OrderItem).
- `totalPrice`: Precio total del pedido.
- `orderType`: Tipo de pedido (`OrderType` enum: en establecimiento, reserva, delivery).
- `status`: Estado del pedido (ej: pendiente, en preparación, completado).
- `specialInstructions`: Instrucciones especiales para el pedido (ej: sin gluten, extra salsa).

**Métodos del endpoint "/order"**:
- `GET(/{id}) getOrder(id)`: (roles permitidos: ADMIN) Devuelve el orderResponseDTO de la id, exepciones: OrderNotFound.
- `GET() getAllOrders()`: (roles permitidos: ADMIN) Devuelve el orderResponseDTO de todas la ordenes.
- `POST() createOrder(orderRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo order, exepciones: OrderAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteOrder(id)`: (roles permitidos: ADMIN) Elimina una order con la id, exepciones:  OrderNotFound.
- `PATCH(/{id}) updateOrder(PatchOrderDTO)`: (roles permitidos: ADMIN) Actualiza una mesa con la id, exepciones:  OrderNotFound, IllegalArgumentException.
- `PATCH(/changestatus/{id}) endOrder()`: (roles permitidos: EMPLOYEE) Cambia el estado de la orden a finalizada y cambia el estado de la mesa o disponibilidad del repartidor, exepciones:  OrderNotFound.

---

#### 6. Delivery
Representa los pedidos que son entregados a domicilio.

**Atributos**:
- `id`: Identificador único de la entrega.
- `client`: Cliente que solicitó el pedido (relación con Client).
- `deliveryAddress`: Dirección donde se entregará el pedido.
- `estimatedDeliveryTime`: Tiempo estimado de entrega.
- `deliveryFee`: Costo del servicio de entrega.
- `status`: Estado del pedido (ej: en preparación, en camino, entregado).
- `order`: Pedido relacionado con la entrega (relación con Order).
- `deliveryPerson`: Empleado que realiza la entrega (relación con Employee).

**Métodos del endpoint "/delivery"**:
- `GET(/{id}) getDelivery(id)`: (roles permitidos: ADMIN) Devuelve el deliveryResponseDTO de la id, exepciones: DeliveryNotFound.
- `GET() getAllDeliverys()`: (roles permitidos: ADMIN) Devuelve el deliveryResponseDTO de todas la ordenes.
- `POST() createDelivery(deliveryRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo delivert, exepciones: DeliveryAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteDelivery(id)`: (roles permitidos: ADMIN) Elimina un delivery con la id, exepciones:  DeliveryrNotFound.
- `PATCH(/{id}) updateDelivery(PatchDeliveryDTO)`: (roles permitidos: ADMIN) Actualiza un delivery con la id, exepciones:  DeliveryNotFound, IllegalArgumentException.
- `PATCH(/changestatus/{id}) endDelivery()`: (roles permitidos: EMPLOYEE) Cambia el estado de la orden a finalizada y cambia el estado de la disponibilidad del repartidor, exepciones: DeliveryNotFound.

---

#### 7. Reservation
Representa las reservas realizadas por los clientes.

**Atributos**:
- `id`: Identificador único de la reserva.
- `client`: Cliente que realizó la reserva (relación con Client).
- `reservationDate`: Fecha en la que se realizará la reserva.
- `reservationTime`: Hora específica de la reserva.
- `numOfPeople`: Número de personas para la reserva.
- `tableNumber`: Número de la mesa asignada para la reserva.
- `status`: Estado de la reserva (ej: pendiente, confirmada, cancelada).
- `specialRequests`: Solicitudes especiales del cliente para la reserva (ej: preferencia de mesa, requerimientos dietéticos).

**Métodos del endpoint "/reservation"**:
- `GET(/{id}) getReservation(id)`: (roles permitidos: ADMIN) Devuelve el reservationResponseDTO de la id, exepciones: ReservationNotFound.
- `GET() getAllReservations()`: (roles permitidos: ADMIN) Devuelve el reservationResponseDTO de todas la ordenes.
- `POST() createReservation(reservationRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo reservation, exepciones: ReservationAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteReservation(id)`: (roles permitidos: ADMIN) Elimina un reservation con la id, exepciones:  ReservationNotFound.
- `PATCH(/{id}) updateReservation(PatchReservationDTO)`: (roles permitidos: ADMIN) Actualiza un reservation con la id, exepciones:  ReservationNotFound, IllegalArgumentException.
- `PATCH(/changestatus/{id}) endReservation()`: (roles permitidos: EMPLOYEE) Finaliza la reservacion, exepciones: ReservationNotFound.

---

#### 8. OrderItem
Representa los productos dentro de un pedido.

**Atributos**:
- `id`: Identificador único del ítem.
- `product`: Producto solicitado (relación con Product).
- `quantity`: Cantidad de este producto.
- `customization`: Personalizaciones o comentarios (ej: sin sal).
- 
**Métodos del endpoint "/orderItem"**:
- `GET(/{id}) getOrderItem(id)`: (roles permitidos: ADMIN) Devuelve el orderItemResponseDTO de la id, exepciones: OrderItemNotFound.
- `GET() getAllOrderItems()`: (roles permitidos: ADMIN) Devuelve el orderItemResponseDTO de todas las OrderItem.
- `POST() createOrderItem(orderItemRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo orderItem, exepciones: OrderItemAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteOrderItem(id)`: (roles permitidos: ADMIN) Elimina un OrderItem con la id, exepciones:  OrderItemNotFound.
- `PATCH(/{id}) updateOrderItem(PatchOrderItemDTO)`: (roles permitidos: ADMIN) Actualiza un orderItem con la id, exepciones:  OrderItemNotFound, IllegalArgumentException.
- `PATCH(/{id}/{idProducto}) agregarProducto(id,idProducto)`: (roles permitidos: ADMIN) Actualiza un order item añadiendo un producto, exepciones:  ProductoNotFound, IllegalArgumentException.

---

#### 9. Product
Representa los productos (platos o bebidas) del menú.

**Atributos**:
- `id`: Identificador único del producto.
- `name`: Nombre del producto.
- `description`: Descripción del producto.
- `price`: Precio del producto.
- `category`: Categoría del producto (ej: entrada, plato principal, bebida).
- `isAvailable`: Disponibilidad del producto.

**Métodos del endpoint "/product"**:
- `GET(/{id}) getProduct(id)`: (roles permitidos: ADMIN) Devuelve el productResponseDTO de la id, exepciones: ProductItemNotFound.
- `GET() getAllProduct()`: (roles permitidos: ADMIN) Devuelve el productResponseDTO de todas los product.
- `POST() createProduct(productRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo product, exepciones: ProductAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteProductItem(id)`: (roles permitidos: ADMIN) Elimina un product con la id, exepciones:  ProductItemNotFound.
- `PATCH(/{id}) updateProduct(PatchProductDTO)`: (roles permitidos: ADMIN) Actualiza un product con la id, exepciones:  ProductItemNotFound, IllegalArgumentException.


---

#### 10. Rating
Representa la evaluación del servicio prestado por el mesero.

**Atributos**:
- `id`: Identificador único de la evaluación.
- `employee`: Mesero evaluado (relación con Employee).
- `client`: Cliente que realiza la evaluación (relación con Client).
- `score`: Puntuación (de 0 a 5 estrellas).
- `feedback`: Comentarios adicionales del cliente.
- `date`: Fecha de la evaluación.

**Métodos del endpoint "/rating"**:
- `GET(/{id}) getRating(id)`: (roles permitidos: ADMIN) Devuelve el ratingResponseDTO de la id, exepciones: RatingItemNotFound.
- `GET() getAllRatings()`: (roles permitidos: ADMIN) Devuelve el ratingResponseDTO de todas los Ratings.
- `POST() createRating(ratingRequestDTO)`: (roles permitidos: ADMIN) Crea un nuevo Rating, exepciones: RatingAlredyExist, IllegalArgumentException.
- `DELETE(/{id}) deleteRatingItem(id)`: (roles permitidos: ADMIN) Elimina un Rating con la id, exepciones:  RatingItemNotFound.
- `PATCH(/{id}) updateRating(PatchRatingDTO)`: (roles permitidos: ADMIN) Actualiza un Rating con la id, exepciones:  RatingItemNotFound, IllegalArgumentException.


---

#### 11. Admin
Representa al administrador del restaurante.

**Atributos adicionales**:

- `accessLevel`: Nivel de acceso del administrador (ej: gestionar empleados, ver reportes).


## Testing y Manejo de Errores

### Niveles de Testing Realizados

1. **Pruebas Unitarias**
   - Verifican la funcionalidad individual de cada componente del sistema.
     - Respuesta de Endpoints
     - Métodos de los Services
     - Verificación de permisos por roles
     - Manejo de excepciones y errores

2. **Pruebas de Integración**
   - Aseguran que los diferentes módulos interactúan correctamente entre sí.
     - Procesos fundamentales del negocio

3. **Pruebas de Sistema**
   - Validan que el sistema completo cumpla con los requisitos funcionales y no funcionales.
     - Simulación de escenarios reales

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
