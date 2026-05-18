import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITarifa, NewTarifa } from '../tarifa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITarifa for edit and NewTarifaFormGroupInput for create.
 */
type TarifaFormGroupInput = ITarifa | PartialWithRequiredKeyOf<NewTarifa>;

type TarifaFormDefaults = Pick<NewTarifa, 'id' | 'activa'>;

type TarifaFormGroupContent = {
  id: FormControl<ITarifa['id'] | NewTarifa['id']>;
  zona: FormControl<ITarifa['zona']>;
  tipoServicio: FormControl<ITarifa['tipoServicio']>;
  precioFirstHora: FormControl<ITarifa['precioFirstHora']>;
  precioHoraAdicional: FormControl<ITarifa['precioHoraAdicional']>;
  minimoHoras: FormControl<ITarifa['minimoHoras']>;
  precioPorKm: FormControl<ITarifa['precioPorKm']>;
  activa: FormControl<ITarifa['activa']>;
};

export type TarifaFormGroup = FormGroup<TarifaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TarifaFormService {
  createTarifaFormGroup(tarifa?: TarifaFormGroupInput): TarifaFormGroup {
    const tarifaRawValue = {
      ...this.getFormDefaults(),
      ...(tarifa ?? { id: null }),
    };
    return new FormGroup<TarifaFormGroupContent>({
      id: new FormControl(
        { value: tarifaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      zona: new FormControl(tarifaRawValue.zona, {
        validators: [Validators.required],
      }),
      tipoServicio: new FormControl(tarifaRawValue.tipoServicio, {
        validators: [Validators.required],
      }),
      precioFirstHora: new FormControl(tarifaRawValue.precioFirstHora, {
        validators: [Validators.required],
      }),
      precioHoraAdicional: new FormControl(tarifaRawValue.precioHoraAdicional, {
        validators: [Validators.required],
      }),
      minimoHoras: new FormControl(tarifaRawValue.minimoHoras, {
        validators: [Validators.required],
      }),
      precioPorKm: new FormControl(tarifaRawValue.precioPorKm),
      activa: new FormControl(tarifaRawValue.activa, {
        validators: [Validators.required],
      }),
    });
  }

  getTarifa(form: TarifaFormGroup): ITarifa | NewTarifa {
    return form.getRawValue() as ITarifa | NewTarifa;
  }

  resetForm(form: TarifaFormGroup, tarifa: TarifaFormGroupInput): void {
    const tarifaRawValue = { ...this.getFormDefaults(), ...tarifa };
    form.reset({
      ...tarifaRawValue,
      id: { value: tarifaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TarifaFormDefaults {
    return {
      id: null,
      activa: false,
    };
  }
}
