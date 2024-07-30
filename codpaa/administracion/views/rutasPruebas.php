<?php

session_start();

if(isset($_SESSION['idUser'])){ ?>
<div class="page-content" style="padding: 15px">
    <div class="content-wrapper">
        <div class="content-inner">
            <div class="content">
                <div class="card">
                    <div class="card-header bg-indigo text-white">
                        <h6 class="card-title">Cargar ruta</h6>
                    </div>
                    <div class="card-body">
                        <form>
                            <fieldset>
                                <div class="form-group row">
                                    <label for="semana_ruta">Semana de carga</label>
            						<div class="col-sm-2">
            							<select class="form-control" title="semana" id="semana_ruta"
            							        data-options="panelHeight:'auto',value:<?= date('W') + 0; ?>">
                                            
                                            <? if($_SESSION['id_perfil'] == 1): ?>
                                            <option value="<?= date('W') - 2; ?>">Semana <?= date('W') - 2; ?></option>
                                            <option value="<?= date('W') - 1; ?>">Semana <?= date('W') - 1; ?></option>
            
                                            <? endif;?>
                                            <option value="<?= date('W') + 0; ?>">Semana <?= date('W') + 0; ?></option>
                                            <option value="<?= date('W') + 1; ?>">Semana <?= date('W') + 1; ?></option>
                                        </select>
            						</div>
            						<div class="col-sm-4">
            						    <div class="text-center">
                							<div class="input-group">
                							    <input type="file" class="form-control-plaintext" id="file_rutas" accept=".xlsx" required>
                							    <span class="input-group-append">
                								    <button type="submit" class="btn btn-primary">Cargar archivo <i class="icon-spinner3 ml-2"></i></button>
                								</span>
                						    </div>
            						    </div>
            						</div>
            						<div class="col">
            						    <div class="text-center">
            								<button type="button"
            								        class="btn btn-primary">
            								        Guardar
            								        <i class="icon-floppy-disk ml-2"></i>
    								        </button>
            							</div>
            						</div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header">
						<h5 class="card-title">Framed bordered</h5>
					</div>
					<div class="card-body">
					    <div class="card card-table table-responsive shadow-none mb-0">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true"></th>
										<th field="A">Id Promotor</th>
										<th field="B">Id Tienda</th>
										<th field="tienda">Sucursal</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>1</td>
										<td>Eugene</td>
										<td>Kopyov</td>
										<td>@Kopyov</td>
									</tr>
									<tr>
										<td>2</td>
										<td>Victoria</td>
										<td>Baker</td>
										<td>@Vicky</td>
									</tr>
									<tr>
										<td>3</td>
										<td>James</td>
										<td>Alexander</td>
										<td>@Alex</td>
									</tr>
									<tr>
										<td>4</td>
										<td>Franklin</td>
										<td>Morrison</td>
										<td>@Frank</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
                </div>
            </div>
        </div>
    </div>
</div>

<?php
    
}else{
        echo 'no has iniciado sesion';
        header('refresh:2,../index.php');
}

?>
