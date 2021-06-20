# Sistemas de Información - Convirtiendo los datos en información


## 0. Demo

![demo](https://user-images.githubusercontent.com/48054735/122683511-f27bd700-d1ff-11eb-96f3-4d8b6a377d4c.gif)

## 1. Objetivo

El objetivo de este proyecto consiste en diseñar y poblar un almacén de datos para después poder ofrecer información útil para la
toma de decisiones mediante OLAP, BI y algoritmos de recomendación.

![dashboard_home](https://user-images.githubusercontent.com/48054735/122682458-b6de0e80-d1f9-11eb-8dbb-f2cb9ed1acee.png)

## 2. Escenario

Un nuevo virus se está extendiendo por la ciudad y las autoridades sanitarias solicitan diseñar un sistema de
información en el que volcar datos de diferentes hospitales y pacientes ingresados en ellos para facilitar la obtención
de información útil y la toma de decisiones.

Los datos de entrada se encuentran en archivos .CSV en el directorio <code>/data</code> y tienen el siguiente esquema:

 - **hospital_*n*.csv**: id ; paciente_id ; Fecha_ingreso ; Duracion ; UCI ; Fallecido ; Tratamiento
 - **pacientes_*n*.csv**: Id ; Edad ; Sexo ; IMC ; FormaFisica ; Tabaquismo ; Alcoholismo ; Colesterol ; Hipertension ; Cardiopatia
   ; Reuma ; EPOC ; Hepatitis ; Cancer
   
donde *n* es el identificador del hospital, en total hay 4 hospitales.

Además, se han usado dos ficheros más para las dimensiones *tiempo* y *lugar*:

 - **dimTiempo.csv**: id ; fecha ; dia ; mes ; anio ; cuatrim ; diasemana ; esfinde
 - **dimLugar.csv**: id ; nombre ; cpostal ; autopista ; gestor

## 3. Implementación

### 3.1. Almacén de datos

La primera fase es el diseño un almacén de datos para poder realizar OLAP sobre los enfermos hospitalizados.

La tabla de hechos recoge información sobre cada ingreso realizado en un hospital, y se enriquece con tres dimensiones: 
Tiempo, Hospital y Paciente.

 - *Tiempo* detalla información del día de ingreso.
 - *Hospital* tiene datos de las características del hospital.
 - *Paciente* contiene una descripción básica del nivel de salud de la persona ingresada.

Se lleva a cabo una implementación ROLAP siguiendo el siguiente esquema:

 - **tablaHECHOS**: id (PK); cliente_id (FK) ; hospital_id (FK) ; fechaIngreso_id (FK) ; Duracion ; UCI ; Fallecido;
   Tratamiento
 - **dimTIEMPO**: id (PK); fecha; dia ; mes ; anio ; cuatrim ; diasemana ; esfinde
 - **dimPACIENTE**: id (PK); edad ; sexo ; IMC ; formaFisica ; tabaquismo ; alcoholismo ; colesterol; hipertension;
   cardiopatia ; reuma ; EPOC ; hepatitis ; cancer
 - **dimHOSPITAL**: id (PK); nombre ; cpostal ; autopista ; gestor
   
### 3.2. ETL

Una vez diseñada la estructura del almacén de datos, se puebla con los ficheros .CSV disponibles. Para
ello se deben crear las clases y métodos adecuados que lean, extraigan, transformen y carguen cada fila en el
almacén de datos. Hay que tener en cuenta que:

 - Todas las tablas dadas tienen un campo “ID”, que es una clave sustituta (surrogate).
 - Puede haber datos que se representen de maneras diferentes según el hospital. Evidentemente habrá que
elegir un modo de unificar la información.
   
### 3.3. Filtrado colaborativo

Los 4 hospitales que están tratando a pacientes a día de hoy están probando hasta con 20 compuestos diferentes
(vitaminas, antiinflamatorios, retrovirales, etc) que provocan diferentes resultados en los pacientes. Como se trata
de una enfermedad nueva, los investigadores aplican una técnica denominada “algoritmo randomizado”, que con-
siste en comenzar asignando aleatoriamente los compuestos a los pacientes para ver sus reacciones y después,
mediante filtrado colaborativo, buscar aquellos que mejor pueden ayudar según la eficacia que han tenido.
Para realizar el filtrado colaborativo, cada hospital han enviado un fichero CSV con la siguiente información en cada fila:
 - Id del paciente en el hospital correspondiente
 - Valoración por parte del médico del resultado que ha tenido cada compuesto en el paciente:
    - <code>0</code> cuando ese compuesto NO haya sido administrado a ese paciente.
     
    - de <code>1</code> a <code>5</code> en caso de que SÍ, donde <code>1</code> es prácticamente nada de efecto y 
      <code>5</code> es mucho efecto (se sobreentiende que el efecto es siempre positivo).
      
Para la implementación de este apartado se ha utilizado la librería Apache Mahout.

![filtrado_colaborativo](https://user-images.githubusercontent.com/48054735/122682067-b3498800-d1f7-11eb-9630-48ed7878b26e.png)
     
### 3.4. Reglas de asociación

A continuación, se crearán reglas de asociación con aquellos compuestos que han tenido éxito. Para
ello se transforman en *true* todas aquellas celdas en los ficheros .CSV del apartado anterior con valor 4 ó 5, y en
*false* las demás.
También se crearán reglas de asociación con aquellos compuestos que no hayan tenido éxito. En este caso
se transforman en *true* todas aquellas celdas en los ficheros .CSV del apartado anterior con valor 1, 2 ó 3, y en
*false* las demás.

Las reglas de asociación obtenidas se presentarán en dos ficheros TXT en el directorio 
<code>/entregables</code>, reglasExito.txt y reglasFallos.txt

Para la implementación de este apartado se ha utilizado Weka, una librería de Machine Learning.

![reglas_asociacion](https://user-images.githubusercontent.com/48054735/122682231-91043a00-d1f8-11eb-8ac0-8ece5e4551e1.png)

### 3.5. Agrupamiento

Se desea agrupar a los pacientes en tres pacientes prototipo: paciente fallecido, paciente ingresados en la UCI y resto 
de pacientes.

Para la implementación de este apartado se ha utilizado el algoritmo *kMeans* de la librería Weka.

![agrupamiento](https://user-images.githubusercontent.com/48054735/122682279-d3c61200-d1f8-11eb-9a08-304acc02a919.png)

## 4. Decisiones de implementación

Véase el documento *memoria_practica3.pdf*
