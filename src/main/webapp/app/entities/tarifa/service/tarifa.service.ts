import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITarifa, NewTarifa } from '../tarifa.model';

export type PartialUpdateTarifa = Partial<ITarifa> & Pick<ITarifa, 'id'>;

@Injectable()
export class TarifasService {
  readonly tarifasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly tarifasResource = httpResource<ITarifa[]>(() => {
    const params = this.tarifasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of tarifa that have been fetched. It is updated when the tarifasResource emits a new value.
   * In case of error while fetching the tarifas, the signal is set to an empty array.
   */
  readonly tarifas = computed(() => (this.tarifasResource.hasValue() ? this.tarifasResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/tarifas');
}

@Injectable({ providedIn: 'root' })
export class TarifaService extends TarifasService {
  protected readonly http = inject(HttpClient);

  create(tarifa: NewTarifa): Observable<ITarifa> {
    return this.http.post<ITarifa>(this.resourceUrl, tarifa);
  }

  update(tarifa: ITarifa): Observable<ITarifa> {
    return this.http.put<ITarifa>(`${this.resourceUrl}/${encodeURIComponent(this.getTarifaIdentifier(tarifa))}`, tarifa);
  }

  partialUpdate(tarifa: PartialUpdateTarifa): Observable<ITarifa> {
    return this.http.patch<ITarifa>(`${this.resourceUrl}/${encodeURIComponent(this.getTarifaIdentifier(tarifa))}`, tarifa);
  }

  find(id: number): Observable<ITarifa> {
    return this.http.get<ITarifa>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITarifa[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITarifa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTarifaIdentifier(tarifa: Pick<ITarifa, 'id'>): number {
    return tarifa.id;
  }

  compareTarifa(o1: Pick<ITarifa, 'id'> | null, o2: Pick<ITarifa, 'id'> | null): boolean {
    return o1 && o2 ? this.getTarifaIdentifier(o1) === this.getTarifaIdentifier(o2) : o1 === o2;
  }

  addTarifaToCollectionIfMissing<Type extends Pick<ITarifa, 'id'>>(
    tarifaCollection: Type[],
    ...tarifasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tarifas: Type[] = tarifasToCheck.filter(isPresent);
    if (tarifas.length > 0) {
      const tarifaCollectionIdentifiers = tarifaCollection.map(tarifaItem => this.getTarifaIdentifier(tarifaItem));
      const tarifasToAdd = tarifas.filter(tarifaItem => {
        const tarifaIdentifier = this.getTarifaIdentifier(tarifaItem);
        if (tarifaCollectionIdentifiers.includes(tarifaIdentifier)) {
          return false;
        }
        tarifaCollectionIdentifiers.push(tarifaIdentifier);
        return true;
      });
      return [...tarifasToAdd, ...tarifaCollection];
    }
    return tarifaCollection;
  }
}
