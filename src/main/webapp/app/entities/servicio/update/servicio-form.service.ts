import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServicio, NewServicio } from '../servicio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServicio for edit and NewServicioFormGroupInput for create.
 */
type ServicioFormGroupInput = IServicio | PartialWithRequiredKeyOf<NewServicio>;

type ServicioFormDefaults = Pick<NewServicio, 'id' | 'trabajadoreses'>;

type ServicioFormGroupContent = {
  id: FormControl<IServicio['id'] | NewServicio['id']>;
  tipoServicio: FormControl<IServicio['tipoServicio']>;
  zona: FormControl<IServicio['zona']>;
  frecuencia: FormControl<IServicio['frecuencia']>;
  fecha: FormControl<IServicio['fecha']>;
  horaInicio: FormControl<IServicio['horaInicio']>;
  duracionHoras: FormControl<IServicio['duracionHoras']>;
  numTrabajadores: FormControl<IServicio['numTrabajadores']>;
  estado: FormControl<IServicio['estado']>;
  direccion: FormControl<IServicio['direccion']>;
  municipio: FormControl<IServicio['municipio']>;
  notas: FormControl<IServicio['notas']>;
  precioTotal: FormControl<IServicio['precioTotal']>;
  descuento: FormControl<IServicio['descuento']>;
  cliente: FormControl<IServicio['cliente']>;
  tarifa: FormControl<IServicio['tarifa']>;
  trabajadoreses: FormControl<IServicio['trabajadoreses']>;
};

export type ServicioFormGroup = FormGroup<ServicioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicioFormService {
  createServicioFormGroup(servicio?: ServicioFormGroupInput): ServicioFormGroup {
    const servicioRawValue = {
      ...this.getFormDefaults(),
      ...(servicio ?? { id: null }),
    };
    return new FormGroup<ServicioFormGroupContent>({
      id: new FormControl(
        { value: servicioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipoServicio: new FormControl(servicioRawValue.tipoServicio, {
        validators: [Validators.required],
      }),
      zona: new FormControl(servicioRawValue.zona, {
        validators: [Validators.required],
      }),
      frecuencia: new FormControl(servicioRawValue.frecuencia, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(servicioRawValue.fecha, {
        validators: [Validators.required],
      }),
      horaInicio: new FormControl(servicioRawValue.horaInicio, {
        validators: [Validators.required, Validators.maxLength(5)],
      }),
      duracionHoras: new FormControl(servicioRawValue.duracionHoras, {
        validators: [Validators.required],
      }),
      numTrabajadores: new FormControl(servicioRawValue.numTrabajadores, {
        validators: [Validators.required],
      }),
      estado: new FormControl(servicioRawValue.estado, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(servicioRawValue.direccion, {
        validators: [Validators.maxLength(200)],
      }),
      municipio: new FormControl(servicioRawValue.municipio, {
        validators: [Validators.maxLength(100)],
      }),
      notas: new FormControl(servicioRawValue.notas, {
        validators: [Validators.maxLength(1000)],
      }),
      precioTotal: new FormControl(servicioRawValue.precioTotal),
      descuento: new FormControl(servicioRawValue.descuento),
      cliente: new FormControl(servicioRawValue.cliente, {
        validators: [Validators.required],
      }),
      tarifa: new FormControl(servicioRawValue.tarifa),
      trabajadoreses: new FormControl(servicioRawValue.trabajadoreses ?? []),
    });
  }

  getServicio(form: ServicioFormGroup): IServicio | NewServicio {
    return form.getRawValue() as IServicio | NewServicio;
  }

  resetForm(form: ServicioFormGroup, servicio: ServicioFormGroupInput): void {
    const servicioRawValue = { ...this.getFormDefaults(), ...servicio };
    form.reset({
      ...servicioRawValue,
      id: { value: servicioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServicioFormDefaults {
    return {
      id: null,
      trabajadoreses: [],
    };
  }
}
