BatchRecetaXML
Ejercicio Recetas - Pasar de xlsx a xml
Se debe elaborar un proceso Spring Batch para averiguar qué recetas podemos o no podemos hacer. Para ello se deben tener en cuenta los siguientes puntos.
1. La lista de ingredientes y su stock debe se obtiene de un fichero ingredientes.csv
2. El libro de recetas está contenido dentro de un fichero recetas.xlsx
3. Se deben procesar las recetas y ver si tenemos disponibles los ingredientes y las cantidades especificadas en cada receta. Lo ingredientes opcionales, como su nombre indica, son opcionales.
4. Debemos exportar las recetas que Sí podemos hacer a un fichero recetas_final.xml y hay que indicar si la receta está completa (dispone del ingrediente opcional) o no.

Estructura de los ficheros
ingredientes.csv
Id;Ingrediente;Cantidad
recetas.xlsx
Id NombreReceta Ingrediente Cantidad IngredienteOpc CantidadOpc
recetas_final.xml
 
![image](https://github.com/user-attachments/assets/2489b604-d8f0-4760-91bc-cac714a789b1)
