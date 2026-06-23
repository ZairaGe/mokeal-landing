import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import RevealDirective from 'app/reveal/reveal.directive';

@Component({
  selector: 'app-contact',
  standalone: true,
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
  RevealDirective]
})
export default class ContactComponent {}