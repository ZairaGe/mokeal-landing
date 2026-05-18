import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TipoServicio } from 'app/entities/enumerations/tipo-servicio.model';
import { ZonaTarifa } from 'app/entities/enumerations/zona-tarifa.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TarifaService } from '../service/tarifa.service';
import { ITarifa } from '../tarifa.model';

import { TarifaFormGroup, TarifaFormService } from './tarifa-form.service';

@Component({
  selector: 'jhi-tarifa-update',
  templateUrl: './tarifa-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TarifaUpdate implements OnInit {
  readonly isSaving = signal(false);
  tarifa: ITarifa | null = null;
  zonaTarifaValues = Object.keys(ZonaTarifa);
  tipoServicioValues = Object.keys(TipoServicio);

  protected tarifaService = inject(TarifaService);
  protected tarifaFormService = inject(TarifaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TarifaFormGroup = this.tarifaFormService.createTarifaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarifa }) => {
      this.tarifa = tarifa;
      if (tarifa) {
        this.updateForm(tarifa);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const tarifa = this.tarifaFormService.getTarifa(this.editForm);
    if (tarifa.id === null) {
      this.subscribeToSaveResponse(this.tarifaService.create(tarifa));
    } else {
      this.subscribeToSaveResponse(this.tarifaService.update(tarifa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITarifa | null>): void {
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

  protected updateForm(tarifa: ITarifa): void {
    this.tarifa = tarifa;
    this.tarifaFormService.resetForm(this.editForm, tarifa);
  }
}
