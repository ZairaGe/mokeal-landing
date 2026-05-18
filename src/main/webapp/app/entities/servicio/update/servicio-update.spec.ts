import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { TarifaService } from 'app/entities/tarifa/service/tarifa.service';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { TrabajadorService } from 'app/entities/trabajador/service/trabajador.service';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';
import { ServicioService } from '../service/servicio.service';
import { IServicio } from '../servicio.model';

import { ServicioFormService } from './servicio-form.service';
import { ServicioUpdate } from './servicio-update';

describe('Servicio Management Update Component', () => {
  let comp: ServicioUpdate;
  let fixture: ComponentFixture<ServicioUpdate>;
  let activatedRoute: ActivatedRoute;
  let servicioFormService: ServicioFormService;
  let servicioService: ServicioService;
  let clienteService: ClienteService;
  let tarifaService: TarifaService;
  let trabajadorService: TrabajadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ServicioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioFormService = TestBed.inject(ServicioFormService);
    servicioService = TestBed.inject(ServicioService);
    clienteService = TestBed.inject(ClienteService);
    tarifaService = TestBed.inject(TarifaService);
    trabajadorService = TestBed.inject(TrabajadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Cliente query and add missing value', () => {
      const servicio: IServicio = { id: 644 };
      const cliente: ICliente = { id: 13484 };
      servicio.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      vitest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      vitest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.clientesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Tarifa query and add missing value', () => {
      const servicio: IServicio = { id: 644 };
      const tarifa: ITarifa = { id: 15454 };
      servicio.tarifa = tarifa;

      const tarifaCollection: ITarifa[] = [{ id: 15454 }];
      vitest.spyOn(tarifaService, 'query').mockReturnValue(of(new HttpResponse({ body: tarifaCollection })));
      const additionalTarifas = [tarifa];
      const expectedCollection: ITarifa[] = [...additionalTarifas, ...tarifaCollection];
      vitest.spyOn(tarifaService, 'addTarifaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(tarifaService.query).toHaveBeenCalled();
      expect(tarifaService.addTarifaToCollectionIfMissing).toHaveBeenCalledWith(
        tarifaCollection,
        ...additionalTarifas.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.tarifasSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Trabajador query and add missing value', () => {
      const servicio: IServicio = { id: 644 };
      const trabajadoreses: ITrabajador[] = [{ id: 528 }];
      servicio.trabajadoreses = trabajadoreses;

      const trabajadorCollection: ITrabajador[] = [{ id: 528 }];
      vitest.spyOn(trabajadorService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajadorCollection })));
      const additionalTrabajadors = [...trabajadoreses];
      const expectedCollection: ITrabajador[] = [...additionalTrabajadors, ...trabajadorCollection];
      vitest.spyOn(trabajadorService, 'addTrabajadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(trabajadorService.query).toHaveBeenCalled();
      expect(trabajadorService.addTrabajadorToCollectionIfMissing).toHaveBeenCalledWith(
        trabajadorCollection,
        ...additionalTrabajadors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.trabajadorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const servicio: IServicio = { id: 644 };
      const cliente: ICliente = { id: 13484 };
      servicio.cliente = cliente;
      const tarifa: ITarifa = { id: 15454 };
      servicio.tarifa = tarifa;
      const trabajadores: ITrabajador = { id: 528 };
      servicio.trabajadoreses = [trabajadores];

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection()).toContainEqual(cliente);
      expect(comp.tarifasSharedCollection()).toContainEqual(tarifa);
      expect(comp.trabajadorsSharedCollection()).toContainEqual(trabajadores);
      expect(comp.servicio).toEqual(servicio);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicio>();
      const servicio = { id: 24037 };
      vitest.spyOn(servicioFormService, 'getServicio').mockReturnValue(servicio);
      vitest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicio);
      saveSubject.complete();

      // THEN
      expect(servicioFormService.getServicio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioService.update).toHaveBeenCalledWith(expect.objectContaining(servicio));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicio>();
      const servicio = { id: 24037 };
      vitest.spyOn(servicioFormService, 'getServicio').mockReturnValue({ id: null });
      vitest.spyOn(servicioService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicio);
      saveSubject.complete();

      // THEN
      expect(servicioFormService.getServicio).toHaveBeenCalled();
      expect(servicioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServicio>();
      const servicio = { id: 24037 };
      vitest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCliente', () => {
      it('should forward to clienteService', () => {
        const entity = { id: 13484 };
        const entity2 = { id: 20795 };
        vitest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTarifa', () => {
      it('should forward to tarifaService', () => {
        const entity = { id: 15454 };
        const entity2 = { id: 1399 };
        vitest.spyOn(tarifaService, 'compareTarifa');
        comp.compareTarifa(entity, entity2);
        expect(tarifaService.compareTarifa).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTrabajador', () => {
      it('should forward to trabajadorService', () => {
        const entity = { id: 528 };
        const entity2 = { id: 4393 };
        vitest.spyOn(trabajadorService, 'compareTrabajador');
        comp.compareTrabajador(entity, entity2);
        expect(trabajadorService.compareTrabajador).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
