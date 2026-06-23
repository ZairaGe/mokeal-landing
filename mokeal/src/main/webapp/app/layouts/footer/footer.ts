import { RouterModule } from '@angular/router';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
  imports: [RouterModule],
})
export default class Footer {
  private router = inject(Router);

  login(): void {
    this.router.navigate(['/login']);
  }
}