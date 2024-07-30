<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <html>
    <head>

    </head>

    <body>
    <?
    $idMensaje = $_REQUEST['idMensaje'];

    include_once('../../connexion/DataBase.php');


    $manager = DataBase::getInstance();

    $query_msj = "SELECT * FROM mensajes WHERE id_mensaje='" . $idMensaje . "'";

    $resul_msj = $manager->ejecutarConsulta($query_msj);

    $datos_msj = mysqli_fetch_array($resul_msj);


    ?>
    <div>
        <form id="fm_editMsj" method="post" enctype="multipart/form-data" class="form-style">

            <ul>
                <li>
                    <label>Marca:</label>

                    <input class="easyui-combobox" name="MarcaMsjE" id="MarcaMsjE"
                                            data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarca.php',required:true, value:'<?= $datos_msj['id_marca'];?>'">

                </li>

                <li>

                    <label for="tipo-promotor" >Tipo promtor</label>
                    <select id="id-tipo-promo" name="tipo-promo" class="easyui-combobox" prompt="Tipo" style="width: 120px;" data-options="value:'<?=$datos_msj['id_tipo_promotor']; ?>'" required>

                        <option value="1">Autoservicio</option>
                        <option value="2">Mayoreo</option>

                    </select>

                </li>

                <li>
                    <label><strong>Titulo:</strong></label>


                        <input type="hidden" name="id_msjdE" id="id_msjE" value="<? echo $idMensaje; ?>">
                        <input name="titulo_msjE" id="titulo_msjE" class="easyui-textbox" required
                               data-options="value:'<?= trim($datos_msj['titulo']); ?>'">
                </li>
                <li>
                    <label><strong>Asunto:</strong></label>

                    <input name="asunto_msjE" id="asunto_msjE" class="easyui-textbox"
                                            data-options="value:'<?= $datos_msj['asunto']; ?>'" required>
                </li>
                <li>
                    <label><strong>Contenido:</strong></label>

                    <input name="contenido_msjE" id="contenido_msjE" class="easyui-textbox"
                                            style=" width:200px; height:100px;"
                                            data-options="value:'<?= $datos_msj['mensaje']; ?>',multiline:true,required:true">
                </li>

            </ul>



        </form>
    </div>

    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>