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
import { EstadoFactura } from 'app/entities/enumerations/estado-factura.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

import { FacturaFormGroup, FacturaFormService } from './factura-form.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class FacturaUpdate implements OnInit {
  readonly isSaving = signal(false);
  factura: IFactura | null = null;
  estadoFacturaValues = Object.keys(EstadoFactura);

  serviciosSharedCollection = signal<IServicio[]>([]);
  clientesSharedCollection = signal<ICliente[]>([]);

  protected facturaService = inject(FacturaService);
  protected facturaFormService = inject(FacturaFormService);
  protected servicioService = inject(ServicioService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacturaFormGroup = this.facturaFormService.createFacturaFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
      if (factura) {
        this.updateForm(factura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const factura = this.facturaFormService.getFactura(this.editForm);
    if (factura.id === null) {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFactura | null>): void {
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

  protected updateForm(factura: IFactura): void {
    this.factura = factura;
    this.facturaFormService.resetForm(this.editForm, factura);

    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, factura.servicio),
    );
    this.clientesSharedCollection.update(clientes =>
      this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, factura.cliente),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.factura?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.factura?.cliente)))
      .subscribe((clientes: ICliente[]) => this.clientesSharedCollection.set(clientes));
  }
}
