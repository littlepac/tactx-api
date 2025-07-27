import React from 'react';
import {CredentialResponse, GoogleLogin} from "@react-oauth/google";

function HomePage(props: { redirect: (credential: string | undefined) => void }) {
    const {redirect} = props;
    return (
        <GoogleLogin onSuccess={(response : CredentialResponse) => redirect(response?.credential)}/>
    );
}

export default HomePage;
