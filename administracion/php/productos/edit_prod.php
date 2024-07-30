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
    $idProducto = $_REQUEST['idProducto'];

    include_once('../../connexion/DataBase.php');
    include_once('../../php/seguridad.php');

    $manager = DataBase::getInstance();

    $query_prod = "SELECT * FROM Producto WHERE idProducto='" . $idProducto . "'";

    $resul_prod = $manager->ejecutarConsulta($query_prod);

    $datos_prod = mysqli_fetch_array($resul_prod);


    ?>
    <div align="center">
        <form id="fm_editProd" method="post" enctype="multipart/form-data">


            <ul>

                <li>
                    <label for="nombre_prodE">Nombre Producto</label>
                    <input name="nombre_prodE" id="nombre_prodE" class="easyui-textbox"
                           required data-options="value:'<? echo trim($datos_prod['nombre']); ?>'">
                    <input type="hidden" name="id_prodE" id="id_prodE" value="<? echo $idProducto; ?>">
                </li>
                <li>
                    <label for="presentacionE">Presentacion</label>
                    <input name="presentacionE" id="presentacionE" class="easyui-textbox"
                           data-options="value:'<? echo $datos_prod['presentacion']; ?>'"
                           required>
                </li>
                <li>
                    <label for="marcae">Marca</label>
                    <input class="easyui-combobox" name="MarcaE" id="MarcaE"
                           data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarcaFull.php'"
                           required>
                </li>
                <li>
                    <label for="codigoE">Codigo Barras</label>
                    <input name="codigoE" id="codigoE" class="easyui-textbox"
                           data-options="value:'<? echo $datos_prod['codigoBarras']; ?>'">
                </li>
                <li>
                    <label for="tipo_prodE">Tipo Producto</label>
                    <input name="tipo_prodE" id="tipo_prodE" class="easyui-textbox"
                           data-options="value:'<? echo $datos_prod['tipo']; ?>'" required>
                </li>
                <li>
                    <label for="modelo_prodE">Modelo</label>
                    <input name="modelo_prodE" id="modelo_prodE" class="easyui-textbox"
                           data-options="value:'<? echo $datos_prod['modelo']; ?>'">
                </li>

                <li>
                    <label for="imagen_prodE">Imagen</label>
                    <input type="file" name="imagen_prodE" id="imagen_prodE">
                </li>
            </ul>


        </form>
    </div>
    <script type="application/javascript">
        $('#MarcaE').combobox(
            {
                onLoadSuccess: function (param) {
                    $(this).combobox('select', '<? echo $datos_prod['idMarca'];?>');
                }

            });
    </script>
    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>