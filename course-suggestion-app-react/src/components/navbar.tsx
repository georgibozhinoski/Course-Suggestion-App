import React from "react";
import {NavLink, Outlet} from "react-router-dom";
import logo from "@/assets/logo.webp"
import avatar from "@/assets/avatar.png"


export function Navbar() {

    return <>
        <div className={'h-20 bg-primary position-fixed'}>
            <div className={"w-full flex items-center justify-between px-10 top-10 position-absolute "} style={{position: 'absolute'}} >

                <img src={logo} alt="logo" className={"h-20 position-absolute top-5 aspect-auto"}/>

                <div className="flex space-x-4 w-auto">
                    <NavLink
                        to="/homepage"
                        className={({isActive}) =>
                            (isActive ? "text-primary bg-white" : "text-white/70 bg-primary")
                            + ' button inline-block h-10 rounded-md p-2 shadow text-center align-middle w-36 text-lg box-border transition'
                        }
                    >
                        Home
                    </NavLink>

                    <NavLink
                        to="/search"
                        className={({isActive}) =>
                            (isActive ? "text-primary bg-white" : "text-white/70 bg-primary")
                            + ' button inline-block h-10 rounded-md p-2 shadow text-center align-middle w-36 text-lg box-border transition'
                        }
                    >
                        Search
                    </NavLink>
                </div>

                <NavLink
                    to="/profile"
                >
                    <img src={avatar} alt="user picture" className={'w-10 h-10 rounded-full'}/>
                </NavLink>


            </div>
        </div>

        <Outlet/>
    </>
}
