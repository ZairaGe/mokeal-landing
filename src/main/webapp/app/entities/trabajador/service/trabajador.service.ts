import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITrabajador, NewTrabajador } from '../trabajador.model';

export type PartialUpdateTrabajador = Partial<ITrabajador> & Pick<ITrabajador, 'id'>;

@Injectable()
export class TrabajadorsService {
  readonly trabajadorsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly trabajadorsResource = httpResource<ITrabajador[]>(() => {
    const params = this.trabajadorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of trabajador that have been fetched. It is updated when the trabajadorsResource emits a new value.
   * In case of error while fetching the trabajadors, the signal is set to an empty array.
   */
  readonly trabajadors = computed(() => (this.trabajadorsResource.hasValue() ? this.trabajadorsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/trabajadors');
}

@Injectable({ providedIn: 'root' })
export class TrabajadorService extends TrabajadorsService {
  protected readonly http = inject(HttpClient);

  create(trabajador: NewTrabajador): Observable<ITrabajador> {
    return this.http.post<ITrabajador>(this.resourceUrl, trabajador);
  }

  update(trabajador: ITrabajador): Observable<ITrabajador> {
    return this.http.put<ITrabajador>(`${this.resourceUrl}/${encodeURIComponent(this.getTrabajadorIdentifier(trabajador))}`, trabajador);
  }

  partialUpdate(trabajador: PartialUpdateTrabajador): Observable<ITrabajador> {
    return this.http.patch<ITrabajador>(`${this.resourceUrl}/${encodeURIComponent(this.getTrabajadorIdentifier(trabajador))}`, trabajador);
  }

  find(id: number): Observable<ITrabajador> {
    return this.http.get<ITrabajador>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITrabajador[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITrabajador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTrabajadorIdentifier(trabajador: Pick<ITrabajador, 'id'>): number {
    return trabajador.id;
  }

  compareTrabajador(o1: Pick<ITrabajador, 'id'> | null, o2: Pick<ITrabajador, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrabajadorIdentifier(o1) === this.getTrabajadorIdentifier(o2) : o1 === o2;
  }

  addTrabajadorToCollectionIfMissing<Type extends Pick<ITrabajador, 'id'>>(
    trabajadorCollection: Type[],
    ...trabajadorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trabajadors: Type[] = trabajadorsToCheck.filter(isPresent);
    if (trabajadors.length > 0) {
      const trabajadorCollectionIdentifiers = trabajadorCollection.map(trabajadorItem => this.getTrabajadorIdentifier(trabajadorItem));
      const trabajadorsToAdd = trabajadors.filter(trabajadorItem => {
        const trabajadorIdentifier = this.getTrabajadorIdentifier(trabajadorItem);
        if (trabajadorCollectionIdentifiers.includes(trabajadorIdentifier)) {
          return false;
        }
        trabajadorCollectionIdentifiers.push(trabajadorIdentifier);
        return true;
      });
      return [...trabajadorsToAdd, ...trabajadorCollection];
    }
    return trabajadorCollection;
  }
}
