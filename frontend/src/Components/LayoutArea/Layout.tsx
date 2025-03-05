import Footer from './Footer';
import Header from './Header';
import Menu from './Menu';
import './Layout.css';

function Layout(): JSX.Element {
    return (
        <div className='Layout'>
            <header>
                <Header />
            </header>
            <div className='content'>
                <menu>
                    <Menu />
                </menu>
                <main>
                  {/* <Routing /> */}
                </main>
            </div>
            <footer>
                <Footer />
            </footer>
        </div>
    );
}

export default Layout;