# Pruebas funcionales - Tercera entrega - Grupo 11

## Datos de la ejecución

- Proyecto: ClubDeportivo.
- Responsable de la ejecución manual: Matías Sebastián Barrientos.
- Fecha de ejecución: 06/11/2026.
- Rama de trabajo: `pruebas-tercera-entrega-seba`, creada desde `main`.
- Entorno: Android Studio, emulador Medium Phone API 37.0.
- Método: pruebas manuales de caja negra sobre la aplicación ejecutada en el emulador.
- Evidencias: capturas en la carpeta relativa `capturas/`.
- Datos utilizados: personas ficticias Sebastian Prueba (40111222) y Mariana Prueba (40222333), además de los registros de ejemplo de la aplicación.

## Objetivo y alcance

Verificar los seis requisitos mínimos de la tercera entrega: registro de socios y no socios, pago mensual, pago diario, consulta de información, listado de vencimientos y validaciones de datos.

El acceso con credenciales válidas y la navegación necesaria para los casos también se realizaron durante la ejecución.

## Resultados

Los 15 casos fueron informados como aprobados por el responsable de las pruebas: el resultado obtenido coincidió con el esperado que se detalla a continuación. Las 15 capturas disponibles acompañan el registro; no reemplazan la descripción del caso. CP-04 no tiene captura específica y CP-10 no tiene captura del formulario previo al guardado. Esos resultados se registran según la confirmación del ejecutor.

### CP-01: Formulario de socio vacío

- **Pasos:** Guardar sin completar campos.
- **Resultado esperado:** Impide el alta y muestra «Ingrese el nombre».
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [01_socio_nombre_vacio.png](capturas/01_socio_nombre_vacio.png)

### CP-02: DNI inválido

- **Pasos:** Ingresar Sebastian Prueba, DNI 123 y apto marcado; guardar.
- **Resultado esperado:** Rechaza el DNI: debe contener 7 u 8 números.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [02_socio_dni_invalido.png](capturas/02_socio_dni_invalido.png)

### CP-03: Apto físico obligatorio

- **Pasos:** Ingresar DNI 40111222, desmarcar apto y guardar.
- **Resultado esperado:** Impide el alta e indica que es obligatorio presentar apto físico.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [03_socio_sin_apto.png](capturas/03_socio_sin_apto.png)

### CP-04: Alta de socio

- **Pasos:** Registrar Sebastian Prueba, DNI 40111222, apto marcado, teléfono y actividad vacíos.
- **Resultado esperado:** Confirma el alta y muestra el socio registrado.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** Sin captura específica. Alta confirmada por el ejecutor; CP-06 y CP-07 aportan evidencia posterior de consulta y pago del socio.

### CP-05: DNI de socio duplicado

- **Pasos:** Intentar registrar nuevamente al mismo socio.
- **Resultado esperado:** Impide duplicar el socio e informa que el DNI ya está registrado.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [05_socio_dni_duplicado.png](capturas/05_socio_dni_duplicado.png)

### CP-06: Consulta del socio

- **Pasos:** Buscar DNI 40111222 en Pagos.
- **Resultado esperado:** Muestra Sebastian Prueba, tipo Socio y cuota pendiente de $45.000.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [06_consulta_socio_cuota_pendiente.png](capturas/06_consulta_socio_cuota_pendiente.png)

### CP-07: Pago mensual

- **Pasos:** Pagar la cuota del socio en efectivo.
- **Resultado esperado:** Genera comprobante de cuota mensual con DNI, medio e importe correctos.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [07_pago_mensual_comprobante.png](capturas/07_pago_mensual_comprobante.png)

### CP-08: Prevención de doble cobro

- **Pasos:** Volver a buscar DNI 40111222 después del pago.
- **Resultado esperado:** No tiene pagos pendientes; no habilita guardar otro pago de la misma cuota.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [08_socio_sin_pagos_pendientes.png](capturas/08_socio_sin_pagos_pendientes.png)

### CP-09: Actividad obligatoria del visitante

- **Pasos:** Completar Mariana Prueba, DNI 40222333 y apto; guardar sin actividad ni horario.
- **Resultado esperado:** Impide registrar la visita y solicita actividad válida.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [09_no_socio_actividad_obligatoria.png](capturas/09_no_socio_actividad_obligatoria.png)

### CP-10: Alta de visita

- **Pasos:** Seleccionar actividad, horario y fecha de hoy; verificar importe automático y guardar.
- **Resultado esperado:** Completa el importe según actividad y confirma el pase diario.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [10_no_socio_alta_correcta.png](capturas/10_no_socio_alta_correcta.png)

### CP-11: Pago diario

- **Pasos:** Buscar DNI 40222333 y pagar la visita con tarjeta de crédito en 3 cuotas.
- **Resultado esperado:** Genera comprobante de Pase diario con importe correspondiente, tarjeta y 3 cuotas.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [11_pago_diario_comprobante.png](capturas/11_pago_diario_comprobante.png)

### CP-12: Segunda visita de la misma persona

- **Pasos:** Registrar otra visita con DNI 40222333 y consultar en Pagos; dejarla sin pagar.
- **Resultado esperado:** Permite una nueva visita del DNI existente y muestra el nuevo importe pendiente.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [Confirmación de segunda visita](capturas/12_segunda_visita_pago_pendiente.png). Aunque el nombre del archivo menciona pago pendiente, la imagen muestra la confirmación del registro; la consulta posterior del pendiente se evidencia en CP-15b.

### CP-13: Vencimientos de hoy

- **Pasos:** Abrir Vencimientos y seleccionar Vencen Hoy.
- **Resultado esperado:** Las fechas corresponden al día del emulador y el número de filas coincide con el contador.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [13_socios_vencen_hoy.png](capturas/13_socios_vencen_hoy.png)

### CP-14: Deudores

- **Pasos:** Seleccionar Deudores.
- **Resultado esperado:** Muestra vencimientos anteriores al día actual; filas y contador coinciden.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [14_socios_deudores.png](capturas/14_socios_deudores.png)

### CP-15: Persistencia

- **Pasos:** Detener con Stop y ejecutar con Run; ingresar y consultar ambos DNI.
- **Resultado esperado:** Conserva al socio 40111222 sin pagos pendientes y la segunda visita de 40222333 pendiente.
- **Resultado obtenido:** Coincide con el esperado, según la ejecución manual confirmada.
- **Estado:** Aprobado.
- **Evidencia:** [15a_persistencia_socio_pago_registrado.png](capturas/15a_persistencia_socio_pago_registrado.png), [15b_persistencia_visita_pendiente.png](capturas/15b_persistencia_visita_pendiente.png)

## Cobertura de requisitos

| Requisito | Casos relacionados |
| --- | --- |
| Registrar socios y no socios | CP-04, CP-05, CP-10, CP-12 |
| Registrar pago mensual de socios | CP-06, CP-07, CP-08 |
| Registrar pago diario de no socios | CP-11, CP-12 |
| Consultar información registrada | CP-06, CP-08, CP-12, CP-15 |
| Mostrar socios cuya cuota vence en la fecha correspondiente | CP-13, CP-14 |
| Validar datos incorrectos o incompletos | CP-01, CP-02, CP-03, CP-05, CP-09 |

## Conclusión y límites

Se completaron 15 casos manuales sin fallas reportadas en los recorridos ejecutados. Se comprobó la persistencia de los datos consultados después de detener y volver a ejecutar la aplicación.

La aprobación corresponde a estos casos y a la copia local probada; no constituye una validación exhaustiva de toda la aplicación. No se registró el SHA del commit de origen durante la sesión.

No se comprobaron en esta ejecución: generación de cuotas de meses siguientes, un mismo socio con deuda anterior y cuota que vence hoy simultáneamente, migración de instalaciones anteriores, impresión real, todos los formatos posibles de entrada ni pagos en efectivo y tarjeta para cada tipo de persona. Tampoco se verificó la persistencia después de reiniciar el dispositivo: CP-15 cubre el reinicio del proceso de la aplicación.

Este aporte documenta pruebas y evidencias; no modifica el código funcional.
