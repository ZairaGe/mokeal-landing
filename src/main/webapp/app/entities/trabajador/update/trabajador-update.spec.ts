import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { TrabajadorService } from '../service/trabajador.service';
import { ITrabajador } from '../trabajador.model';

import { TrabajadorFormService } from './trabajador-form.service';
import { TrabajadorUpdate } from './trabajador-update';

describe('Trabajador Management Update Component', () => {
  let comp: TrabajadorUpdate;
  let fixture: ComponentFixture<TrabajadorUpdate>;
  let activatedRoute: ActivatedRoute;
  let trabajadorFormService: TrabajadorFormService;
  let trabajadorService: TrabajadorService;
  let servicioService: ServicioService;

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

    fixture = TestBed.createComponent(TrabajadorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trabajadorFormService = TestBed.inject(TrabajadorFormService);
    trabajadorService = TestBed.inject(TrabajadorService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Servicio query and add missing value', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const servicioses: IServicio[] = [{ id: 24037 }];
      trabajador.servicioses = servicioses;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [...servicioses];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trabajador: ITrabajador = { id: 4393 };
      const servicios: IServicio = { id: 24037 };
      trabajador.servicioses = [servicios];

      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      expect(comp.serviciosSharedCollection()).toContainEqual(servicios);
      expect(comp.trabajador).toEqual(trabajador);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITrabajador>();
      const trabajador = { id: 528 };
      vitest.spyOn(trabajadorFormService, 'getTrabajador').mockReturnValue(trabajador);
      vitest.spyOn(trabajadorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(trabajador);
      saveSubject.complete();

      // THEN
      expect(trabajadorFormService.getTrabajador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trabajadorService.update).toHaveBeenCalledWith(expect.objectContaining(trabajador));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITrabajador>();
      const trabajador = { id: 528 };
      vitest.spyOn(trabajadorFormService, 'getTrabajador').mockReturnValue({ id: null });
      vitest.spyOn(trabajadorService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(trabajador);
      saveSubject.complete();

      // THEN
      expect(trabajadorFormService.getTrabajador).toHaveBeenCalled();
      expect(trabajadorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITrabajador>();
      const trabajador = { id: 528 };
      vitest.spyOn(trabajadorService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trabajadorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
