import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import RevealDirective from 'app/reveal/reveal.directive';

@Component({
    selector: 'app-reviews',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
    RevealDirective],

    templateUrl: './reviews.component.html',
    styleUrl: './reviews.component.scss',

})
export default class ReviewsComponent { }