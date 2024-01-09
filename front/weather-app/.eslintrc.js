module.exports = {
  env: {
    browser: true,
    es2021: true,
    'jest/globals': true,
  },
  extends: ['airbnb', 'plugin:jest/all'],
  overrides: [
    {
      env: {
        node: true,
      },
      files: [
        '.eslintrc.{js,cjs}',
      ],
      parserOptions: {
        sourceType: 'script',
      },
    },
  ],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  rules: {
    'react/prop-types': 0,
    'no-console': 'off',
    'jest/no-hooks': [
      'error',
      {
        allow: [
          'afterEach',
          'beforeEach',
        ],
      },
    ],
  },
  plugins: [
    'jest',
  ],
};
