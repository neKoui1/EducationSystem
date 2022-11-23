import '@arco-design/web-react/dist/css/arco.css';
import type { AppProps } from 'next/app';
import '../styles/globals.css';

export default function App({ Component, pageProps }: AppProps) {
	return <Component {...pageProps} />;
}
