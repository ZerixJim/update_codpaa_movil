<?
//ob_start();
session_start();

if (isset($_SESSION['idUser'])) {
    //si no está logueado lo envío a la página de autentificación
    //sino, calculamos el tiempo transcurrido
    $fechaGuardada = $_SESSION["ultimoAcceso"];
    $ahora = date("Y-n-j H:i:s");
    $tiempo_transcurrido = (strtotime($ahora) - strtotime($fechaGuardada));

    //comparamos el tiempo transcurrido
    if ($tiempo_transcurrido >= 1800) {
        //si pasaron 30 minutos o más

        session_destroy(); // destruyo la sesión
        session_unset();

        //header("Location: ../index.php"); //envío al usuario a la pag. de autenticación

        /* echo "<script>
        $.messager.alert('Sesion','Tiempo de Sesion Terminado, Vuelve a Ingresar','info', function(){
            location.href='../index.php';
       });
          </script>";*/

        echo true;
        //sino, actualizo la fecha de la sesión
    } else {
        $_SESSION["ultimoAcceso"] = $ahora;

        echo false;
    }
} else {
    /*echo "<script>
     $.messager.alert('Sesion','Tiempo de Sesion Terminado, Vuelve a Ingresar','info', function(){
         location.href='../index.php';
    });
       </script>";*/

    echo true;
}

