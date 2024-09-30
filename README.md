# Sistema Digital de Gestión de Pedidos y Evaluación de Servicio para Restaurantes con QR

## Contexto  
La experiencia en restaurantes está evolucionando con la digitalización de los procesos. Este proyecto responde a la necesidad de mejorar la eficiencia en la toma de pedidos y la interacción entre los clientes y los meseros. Aunque el mesero sigue desempeñando un papel crucial, desde recibir a los clientes hasta entregar los pedidos y resolver dudas, el sistema busca facilitar el acceso al menú y optimizar el proceso de pedidos. Además, se incluye un método automatizado de evaluación del servicio mediante códigos QR, lo que permite a los administradores obtener retroalimentación precisa para mejorar la atención al cliente y premiar el buen desempeño de los meseros.

## Objetivos del Proyecto
- Facilitar a los clientes la visualización del menú y la realización de pedidos mediante el escaneo de un código QR en la mesa, sin reemplazar el rol del mesero, quien continuará recepcionando a los clientes, entregando los pedidos y atendiéndolos en sus necesidades adicionales.
- Proporcionar una vía para que los clientes califiquen el servicio de los meseros al finalizar su consumo, mediante un segundo código QR que estará en la boleta, permitiendo a los administradores obtener comentarios y calificaciones de manera rápida y efectiva.
- Otorgar al administrador herramientas para la gestión del desempeño de los meseros, basadas en los comentarios y calificaciones de los clientes, para incentivar buenas prácticas mediante bonificaciones o menciones especiales.

## Justificación del Problema
El sistema busca resolver la necesidad de eficiencia y precisión en la toma de pedidos, a la vez que mantiene el contacto personal entre el mesero y el cliente. La intervención del mesero sigue siendo clave, ya que es quien recibe a los clientes, ofrece la atención durante su estancia y entrega los pedidos. Al mismo tiempo, la implementación de un sistema de evaluación mediante códigos QR permite al administrador recopilar comentarios precisos sobre el desempeño de cada mesero, incentivando la mejora continua y asegurando una experiencia de calidad para los clientes.

## Funcionalidades Clave

### 1. QR en la mesa para acceso al menú y pedidos
Cada mesa contará con un código QR que, al ser escaneado por los clientes, los redirigirá a la página web o aplicativo del restaurante. Desde allí, podrán visualizar el menú, seleccionar sus opciones y enviar el pedido de manera digital. El mesero continuará desempeñando su rol, recepcionando a los clientes, guiándolos a sus mesas y entregando los pedidos realizados.

### 2. Carrito de compras virtual
A través del sistema, los clientes podrán agregar platos y bebidas al carrito, modificar cantidades y personalizar sus pedidos antes de enviarlos. El pedido se registra en el sistema para su preparación y el mesero se encarga de entregarlo en la mesa.

### 3. Generación automática de QR personalizado para meseros
Cada mesero tendrá un código QR personalizado que se incluirá en la boleta de pago del cliente. Esto permitirá a los comensales calificar el servicio recibido al final de su experiencia. El nombre del mesero se autocompleta al escanear este QR para facilitar la evaluación.

### 4. Sistema de evaluación del servicio
Después de pagar, los clientes podrán escanear el código QR en la boleta y dejar una evaluación del servicio prestado por el mesero, así como comentarios sobre su experiencia en general. La evaluación incluirá una calificación de 0 a 5 estrellas, que permitirá al administrador evaluar el desempeño de los meseros.

### 5. Panel de control para el administrador
El administrador podrá acceder a un panel donde se mostrarán los comentarios y calificaciones recibidas por cada mesero. Con esta información, podrá reconocer a los empleados destacados con bonificaciones o menciones honoríficas, como "Empleado del Mes", creando un entorno laboral más justo y motivador.


## Observaciones

Muy buena propuesta de proyecto. Solo algunos puntos a considerar:

1. Deberían agregar alguna opción de seguridad que detecte que los clientes están dentro de un restaurante para que la aplicación diferencie la funcionalidad de pedido en establecimiento vs la reserva vs el delivery.
2. Tengo claro que habrá un rol de administrador para que ingrese a la plataforma. Pero, ¿han pensado en aplicar también un sistema de registro de usuarios? Tómenlo en cuenta.
