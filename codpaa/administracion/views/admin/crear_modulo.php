<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 23/01/2018
 * Time: 11:32 AM
 */

session_start();



if (isset($_SESSION['idUser'])){

?>



    <div id="modulo-nuevo-content" style="height: 100%;">


        <table id="modulos-datagrid"></table>


    </div>



    <script type="text/javascript">

        $('#modulos-datagrid').datagrid({
            url:'../../php/admin/modulos.php',
            fit:'true',

            columns:[[
                {field:'id_menu', title:'id menu'},
                {field:'menu', title:'Menu'},
                {field:'categoria', title:'Categoria'}
            ]],
            toolbar:[{
                text:'Crear modulo',
                iconCls:'icon-edit',
                handler:function () {


                }
            },{
                text:'Editar Modulo',
                iconCls:'icon-edit',
                handler:function () {

                }
            }]


        });


    </script>



<?

}else{
    echo 'acceso denegado';
}


?>