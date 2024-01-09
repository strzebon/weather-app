module.exports = {
  env: {
    browser: true,
    es2021: true,
    'jest/globals': true,
  },
  extends: ['airbnb', 'plugin:jest/all', 'eslint:recommended', 'plugin:react/recommended'],
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
    'react/jsx-filename-extension': [1, { extensions: ['.jsx'] }],
  },
  plugins: [
    'jest',
    'react',
  ],
};
