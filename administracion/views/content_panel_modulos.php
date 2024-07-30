<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 14/08/14

 * Time: 9:26

 */



ob_start();

session_start();

include_once('../connexion/bdManager.php');



if(isset($_SESSION['idUser'])){


    $manager = new bdManager;

    $consulta = "Select mo.nombreModulo,mo.descripcion,mo.icono,mo.modulophp from cod_modulo as mo inner join cod_modulo_permiso as mop on mo.idModulo=mop.idModulo where mop.idUsuario=".$_SESSION['idUser'];

    $resultado = $manager->ejecutarConsulta($consulta);



    while($fila = mysqli_fetch_array($resultado)){

        $modulo = "'".$fila['modulophp']."'";

        $icon = "'icon-large-".$fila['icono']."'";

        $tamanio = "'large'";

        $iconaling = "'top'";

        echo '<a href="javascript:void(0)" class="easyui-linkbutton" style="margin-right:4px;" data-options="iconCls:'.$icon.',size:'.$tamanio.',iconAlign:'.$iconaling.'" onclick="addTap('.$modulo.')">'.$fila['nombreModulo'].'</a>';

    }


}else{

    echo "Sesion no iniciada";

}











