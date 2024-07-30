<? session_start(); ?>
<?
include_once('../connexion/bdManager.php');
$idUsuario = $_SESSION['idUser'] ?>



<form name="form1" method="post" action="" class="form-style" style="padding: 20px;">


    <ul>
        <li>
            Cambio de contraseña

        </li>
        <li>
            <label for="passwd">Contraseña</label>

            <input name="passwd" type="password" class="textfieldform" id="passwd" onchange="testPassword();"
                   onkeyup="testPassword();" size="20"/>

        </li>
        <li>

            <label for="passwd2">Confirmar Contraseña</label>
            <input type="password" id="passwd2" size="20"/>

        </li>
        <li>
            <label for="verdict">Intensidad de la contraseña</label>
            <input name="verdict" type="text" class="textfieldform" id="verdict" readonly size="20"
                   style="background-color:#fa6e6e; font-weight:bold;"/>
        </li>
        <li>
            <label for="save"></label>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="validarPasswd()">Guardar</a>

        </li>
        <li>

            Use al menos 8 digitos, tambien numeros, y signos.

        </li>
    </ul>


</form>
