<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <html>
    <head>

        <script language="javascript" type="application/javascript">


        </script>

    </head>

    <body>
    <?
    $id_promo = $_REQUEST['idPromotor'];

    include_once('../../connexion/DataBase.php');
    $manager = DataBase::getInstance();

    ?>
    <div align="center">
        <div id="ruta_tabs" class="easyui-tabs" style="width:100%;height:270px;"
             data-options="justified:true,narrow:true,plain:true">
            <!---------------------------------LUNES ------------------------------------>
            <div title="Lunes" style="padding:20px; width:100%;height:100%;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato,
                    group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                    
                    from rutasPromotores rp 
                    inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                    inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                    left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                    left join Marca mar on (mar.idMarca = tm.id_marca) 
                        where rp.lunes>='1' and  rp.idPromotor='" . $id_promo . "'
                        group by t.idTienda
                        
                        ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);
                $tiendas_lunes = array();
                ?>
                <table width="95%" border="0" cellspacing="2" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                            <span>
                            Sucursal
                            </span>
                        </td>
                        <td>
                            <span>
                            Cadena
                            </span>
                        </td>
                        <td>
                            <span>
                            Direccion
                            </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {

                        echo "<tr>
                                <td>
                                    <span>" . $datos_ruta['sucursal'] . "</span>
                                </td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";

                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                        ."</tr>";

                        $tiendas_lunes[] = $datos_ruta;

                    }

                    $tiendas_lunes = json_encode($tiendas_lunes, JSON_PARTIAL_OUTPUT_ON_ERROR);


                    ?>
                </table>

            </div>

            <!-------------------------------------Martes ------------------------------->

            <div title="Martes" style="overflow:auto;padding:20px;">
                <?
                $query_ruta2 = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                        group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 

                                where rp.martes>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                
                                ";

                $resul_ruta2 = $manager->ejecutarConsulta($query_ruta2);
                $tiendas_martes = array();
                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta2 = mysqli_fetch_array($resul_ruta2)) {
                        echo "<tr>
                        <td><span>" . $datos_ruta2['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta2['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta2['direcc'] . "</span></td>";


                        echo "<td><span>" . $datos_ruta2['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_martes[] = $datos_ruta2;

                    }

                    $tiendas_martes = json_encode($tiendas_martes, JSON_PARTIAL_OUTPUT_ON_ERROR);

                    ?>
                </table>

            </div>

            <!---------------------------------------------Miercoles --------------------------------------->
            <div title="Miercoles" style="overflow:auto;padding:20px;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 
                                where rp.miercoles>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);
                $tiendas_miercoles = array();
                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {
                        echo "<tr>
                        <td><span>" . $datos_ruta['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";


                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_miercoles[] = $datos_ruta;
                    }

                    $tiendas_miercoles = json_encode($tiendas_miercoles, JSON_PARTIAL_OUTPUT_ON_ERROR);
                    ?>
                </table>
            </div>

            <!-------------------------------------Jueves ----------------------------------------->
            <div title="Jueves" style="overflow:auto;padding:20px;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 
                                where rp.jueves>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);
                $tiendas_jueves = array();
                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {
                        echo "<tr>
            <td><span>" . $datos_ruta['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_jueves[] = $datos_ruta;
                    }

                    $tiendas_jueves = json_encode($tiendas_jueves, JSON_PARTIAL_OUTPUT_ON_ERROR);
                    ?>
                </table>
            </div>

            <!-----------------------------------------------Viernes ------------------------------------------->
            <div title="Viernes" style="overflow:auto;padding:20px;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 
                                where rp.viernes>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);
                $tiendas_viernes = array();

                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {
                        echo "<tr>
            <td><span>" . $datos_ruta['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_viernes[] = $datos_ruta;
                    }

                    $tiendas_viernes = json_encode($tiendas_viernes, JSON_PARTIAL_OUTPUT_ON_ERROR);
                    ?>
                </table>
            </div>

            <!------------------------------------------------------Sabado ---------------------------------------->
            <div title="Sabado" style="overflow:auto;padding:20px;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 
                                where rp.sabado>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);

                $tiendas_sabado = array();
                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {
                        echo "<tr>
            <td><span>" . $datos_ruta['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_sabado[] = $datos_ruta;

                    }

                    $tiendas_sabado = json_encode($tiendas_sabado, JSON_PARTIAL_OUTPUT_ON_ERROR);
                    ?>
                </table>
            </div>

            <!------------------------------------------------Domingo -------------------------------------------->
            <div title="Domingo" style="overflow:auto;padding:20px;">
                <?
                $query_ruta = "Select t.sucursal,concat(t.direccion,' colonia:',t.colonia) as direcc,f.grupo as formato,t.x,t.y, t.idFormato ,
                                group_concat(mar.nombre order by mar.nombre separator ' / ') marcas
                                from rutasPromotores rp 
                                inner join maestroTiendas t on (rp.idTienda=t.idTienda)
                                inner join tiendas_formatos f on (t.idFormato=f.idFormato)
                                left join  tienda_marca tm on (tm.id_tienda = t.idTienda)
                                left join Marca mar on (mar.idMarca = tm.id_marca) 
                                where rp.domingo>='1' and rp.idPromotor='" . $id_promo . "'
                                group by t.idTienda 
                                ";

                $resul_ruta = $manager->ejecutarConsulta($query_ruta);

                $tiendas_domingo = array();
                ?>
                <table width="95%" border="0" cellspacing="0" cellpadding="2" class="table-style-1">
                    <tr>
                        <td>
                <span>
                Sucursal
                </span>
                        </td>
                        <td>
                <span>
                Cadena
                </span>
                        </td>
                        <td>
                <span>
                Direccion
                </span>
                        </td>

                        <td>
                            <span>
                                Marcas
                            </span>
                        </td>
                    </tr>
                    <?
                    while ($datos_ruta = mysqli_fetch_array($resul_ruta)) {
                        echo "<tr>
            <td><span>" . $datos_ruta['sucursal'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['formato'] . "</span></td>";
                        echo "<td><span>" . $datos_ruta['direcc'] . "</span></td>";


                        echo "<td><span>" . $datos_ruta['marcas'] . "</span></td>"
                            ."</tr>";

                        $tiendas_domingo[] = $datos_ruta;
                    }

                    $tiendas_domingo = json_encode($tiendas_domingo, JSON_PARTIAL_OUTPUT_ON_ERROR);
                    ?>
                </table>
            </div>
        </div>
        <!------------------------------------Final Tabs --------------------------------->

        <div id='Mapa-Ruta' style="width:100%;height:275px;"></div>
    </div>
    <script>
        $('#ruta_tabs').tabs({
            onSelect: function (title) {

                switch (title) {
                    case 'Lunes':
                        return ruta_tiendas(<?= $tiendas_lunes;?>);
                        break;

                    case 'Martes':
                        return ruta_tiendas(<?= $tiendas_martes;?>);
                        break;

                    case 'Miercoles':
                        return ruta_tiendas(<?= $tiendas_miercoles;?>);
                        break;

                    case 'Jueves':
                        return ruta_tiendas(<?= $tiendas_jueves;?>);
                        break;

                    case 'Viernes':
                        return ruta_tiendas(<?= $tiendas_viernes;?>);
                        break;

                    case 'Sabado':
                        return ruta_tiendas(<?= $tiendas_sabado;?>);
                        break;

                    case 'Domingo':
                        return ruta_tiendas(<?= $tiendas_domingo;?>);
                        break;
                }

            },
            onUnselect: function (title) {
                deleteMarkers();
            }
        });
    </script>


    <style type="text/css">

        .table-style-1 tr:nth-child(odd) {
            background-color: #d0e4fb;
        }


    </style>

    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>