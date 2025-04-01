import Header from './Header';
import './Layout.css';
import Routing from '../Routing/Routing';
import Menu from './Menu';

function Layout(): JSX.Element {
    return (
        <div className='Layout'>
            <header>
                <Header />
                <nav>
                    <Menu />
                </nav>
            </header>
            <div className='content'>
                <main>
                  <Routing />
                </main>
            </div>
        </div>
    );
}

export default Layout;