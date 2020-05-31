import { Routes } from '@angular/router';

import { UserProfileComponent } from '../../user-profile/user-profile.component';
import { TableListComponent } from '../../table-list/table-list.component';
import { DetailsComponent } from '../../details/details.component';
import { AggregateComponent } from 'app/aggregate/aggregate.component';

export const AdminLayoutRoutes: Routes = [
   
    { path: 'user-profile',   component: UserProfileComponent },
    { path: 'simulations',     component: TableListComponent },
    { path: 'details/:id',     component: DetailsComponent },
    { path: 'aggregate/:dice/:sides',          component: AggregateComponent },
];
