module.exports = [
  {
    context: ['/login', '/roles', '/users'],
    target: 'http://localhost:9001',
    secure: false,
    timeout: 10000,
  }
];
