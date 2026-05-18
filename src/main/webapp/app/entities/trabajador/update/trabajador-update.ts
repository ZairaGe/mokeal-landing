import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TrabajadorService } from '../service/trabajador.service';
import { ITrabajador } from '../trabajador.model';

import { TrabajadorFormGroup, TrabajadorFormService } from './trabajador-form.service';

@Component({
  selector: 'jhi-trabajador-update',
  templateUrl: './trabajador-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TrabajadorUpdate implements OnInit {
  readonly isSaving = signal(false);
  trabajador: ITrabajador | null = null;

  serviciosSharedCollection = signal<IServicio[]>([]);

  protected trabajadorService = inject(TrabajadorService);
  protected trabajadorFormService = inject(TrabajadorFormService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrabajadorFormGroup = this.trabajadorFormService.createTrabajadorFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trabajador }) => {
      this.trabajador = trabajador;
      if (trabajador) {
        this.updateForm(trabajador);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const trabajador = this.trabajadorFormService.getTrabajador(this.editForm);
    if (trabajador.id === null) {
      this.subscribeToSaveResponse(this.trabajadorService.create(trabajador));
    } else {
      this.subscribeToSaveResponse(this.trabajadorService.update(trabajador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITrabajador | null>): void {
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

  protected updateForm(trabajador: ITrabajador): void {
    this.trabajador = trabajador;
    this.trabajadorFormService.resetForm(this.editForm, trabajador);

    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, ...(trabajador.servicioses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, ...(this.trabajador?.servicioses ?? [])),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
