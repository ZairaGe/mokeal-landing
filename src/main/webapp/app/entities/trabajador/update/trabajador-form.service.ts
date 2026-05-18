import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITrabajador, NewTrabajador } from '../trabajador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrabajador for edit and NewTrabajadorFormGroupInput for create.
 */
type TrabajadorFormGroupInput = ITrabajador | PartialWithRequiredKeyOf<NewTrabajador>;

type TrabajadorFormDefaults = Pick<NewTrabajador, 'id' | 'activo' | 'servicioses'>;

type TrabajadorFormGroupContent = {
  id: FormControl<ITrabajador['id'] | NewTrabajador['id']>;
  nombre: FormControl<ITrabajador['nombre']>;
  telefono: FormControl<ITrabajador['telefono']>;
  email: FormControl<ITrabajador['email']>;
  activo: FormControl<ITrabajador['activo']>;
  notas: FormControl<ITrabajador['notas']>;
  servicioses: FormControl<ITrabajador['servicioses']>;
};

export type TrabajadorFormGroup = FormGroup<TrabajadorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrabajadorFormService {
  createTrabajadorFormGroup(trabajador?: TrabajadorFormGroupInput): TrabajadorFormGroup {
    const trabajadorRawValue = {
      ...this.getFormDefaults(),
      ...(trabajador ?? { id: null }),
    };
    return new FormGroup<TrabajadorFormGroupContent>({
      id: new FormControl(
        { value: trabajadorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(trabajadorRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      telefono: new FormControl(trabajadorRawValue.telefono, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      email: new FormControl(trabajadorRawValue.email, {
        validators: [Validators.maxLength(100)],
      }),
      activo: new FormControl(trabajadorRawValue.activo, {
        validators: [Validators.required],
      }),
      notas: new FormControl(trabajadorRawValue.notas, {
        validators: [Validators.maxLength(500)],
      }),
      servicioses: new FormControl(trabajadorRawValue.servicioses ?? []),
    });
  }

  getTrabajador(form: TrabajadorFormGroup): ITrabajador | NewTrabajador {
    return form.getRawValue() as ITrabajador | NewTrabajador;
  }

  resetForm(form: TrabajadorFormGroup, trabajador: TrabajadorFormGroupInput): void {
    const trabajadorRawValue = { ...this.getFormDefaults(), ...trabajador };
    form.reset({
      ...trabajadorRawValue,
      id: { value: trabajadorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TrabajadorFormDefaults {
    return {
      id: null,
      activo: false,
      servicioses: [],
    };
  }
}
