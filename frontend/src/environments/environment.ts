export const environment = {
  production: window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1',
  apiUrl: (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
    ? 'http://localhost:8080/api/v1'
    : 'https://kupi-prodaj-ecommerece.onrender.com/api/v1'
};