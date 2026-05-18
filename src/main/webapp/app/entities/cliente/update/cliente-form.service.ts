import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id' | 'activo'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  nombre: FormControl<ICliente['nombre']>;
  telefono: FormControl<ICliente['telefono']>;
  email: FormControl<ICliente['email']>;
  nifCif: FormControl<ICliente['nifCif']>;
  direccion: FormControl<ICliente['direccion']>;
  municipio: FormControl<ICliente['municipio']>;
  codigoPostal: FormControl<ICliente['codigoPostal']>;
  notas: FormControl<ICliente['notas']>;
  activo: FormControl<ICliente['activo']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente?: ClienteFormGroupInput): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...(cliente ?? { id: null }),
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(clienteRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      telefono: new FormControl(clienteRawValue.telefono, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      email: new FormControl(clienteRawValue.email, {
        validators: [Validators.maxLength(100)],
      }),
      nifCif: new FormControl(clienteRawValue.nifCif, {
        validators: [Validators.maxLength(20)],
      }),
      direccion: new FormControl(clienteRawValue.direccion, {
        validators: [Validators.maxLength(200)],
      }),
      municipio: new FormControl(clienteRawValue.municipio, {
        validators: [Validators.maxLength(100)],
      }),
      codigoPostal: new FormControl(clienteRawValue.codigoPostal, {
        validators: [Validators.maxLength(10)],
      }),
      notas: new FormControl(clienteRawValue.notas, {
        validators: [Validators.maxLength(500)],
      }),
      activo: new FormControl(clienteRawValue.activo, {
        validators: [Validators.required],
      }),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset({
      ...clienteRawValue,
      id: { value: clienteRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
