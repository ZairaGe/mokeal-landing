import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { TipoServicio } from 'app/entities/enumerations/tipo-servicio.model';
import { TarifaService } from 'app/entities/tarifa/service/tarifa.service';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { ServicioService } from '../service/servicio.service';
import { IServicio } from '../servicio.model';

import { ServicioFormGroup, ServicioFormService } from './servicio-form.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { ZonaTarifa } from 'app/entities/enumerations/zona-tarifa.model';
import { Frecuencia } from 'app/entities/enumerations/frecuencia.model';
import { EstadoServicio } from 'app/entities/enumerations/estado-servicio.model';

@Component({
  selector: 'jhi-servicio-update',
  templateUrl: './servicio-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class ServicioUpdate implements OnInit {
  readonly isSaving = signal(false);
  servicio: IServicio | null = null;
  tipoServicioValues = Object.keys(TipoServicio);
  zonaTarifaValues = Object.keys(ZonaTarifa);
  frecuenciaValues = Object.keys(Frecuencia);
  estadoServicioValues = Object.keys(EstadoServicio);

  clientesSharedCollection = signal<ICliente[]>([]);
  tarifasSharedCollection = signal<ITarifa[]>([]);
  trabajadorsSharedCollection = signal<ITrabajador[]>([]);

  protected servicioService = inject(ServicioService);
  protected servicioFormService = inject(ServicioFormService);
  protected clienteService = inject(ClienteService);
  protected tarifaService = inject(TarifaService);
  protected trabajadorService = inject(TrabajadorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServicioFormGroup = this.servicioFormService.createServicioFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareTarifa = (o1: ITarifa | null, o2: ITarifa | null): boolean => this.tarifaService.compareTarifa(o1, o2);

  compareTrabajador = (o1: ITrabajador | null, o2: ITrabajador | null): boolean => this.trabajadorService.compareTrabajador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicio }) => {
      this.servicio = servicio;
      if (servicio) {
        this.updateForm(servicio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const servicio = this.servicioFormService.getServicio(this.editForm);
    if (servicio.id === null) {
      this.subscribeToSaveResponse(this.servicioService.create(servicio));
    } else {
      this.subscribeToSaveResponse(this.servicioService.update(servicio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServicio | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(servicio: IServicio): void {
    this.servicio = servicio;
    this.servicioFormService.resetForm(this.editForm, servicio);

    this.clientesSharedCollection.update(clientes =>
      this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, servicio.cliente),
    );
    this.tarifasSharedCollection.update(tarifas => this.tarifaService.addTarifaToCollectionIfMissing<ITarifa>(tarifas, servicio.tarifa));
    this.trabajadorsSharedCollection.update(trabajadors =>
      this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(servicio.trabajadoreses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.servicio?.cliente)))
      .subscribe((clientes: ICliente[]) => this.clientesSharedCollection.set(clientes));

    this.tarifaService
      .query()
      .pipe(map((res: HttpResponse<ITarifa[]>) => res.body ?? []))
      .pipe(map((tarifas: ITarifa[]) => this.tarifaService.addTarifaToCollectionIfMissing<ITarifa>(tarifas, this.servicio?.tarifa)))
      .subscribe((tarifas: ITarifa[]) => this.tarifasSharedCollection.set(tarifas));

    this.trabajadorService
      .query()
      .pipe(map((res: HttpResponse<ITrabajador[]>) => res.body ?? []))
      .pipe(
        map((trabajadors: ITrabajador[]) =>
          this.trabajadorService.addTrabajadorToCollectionIfMissing<ITrabajador>(trabajadors, ...(this.servicio?.trabajadoreses ?? [])),
        ),
      )
      .subscribe((trabajadors: ITrabajador[]) => this.trabajadorsSharedCollection.set(trabajadors));
  }
}
