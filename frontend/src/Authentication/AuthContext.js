import React from 'react';

export const anonymous = {auth : {
    isAuthenticated: false,
    user: null,
    token: null,
  }};
export const AuthContext = React.createContext(anonymous);