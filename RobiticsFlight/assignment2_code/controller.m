function [ F, M ] = controller(~, state, des_state, params)
%CONTROLLER  Controller for the planar quadrotor
%
%   state: The current state of the robot with the following fields:
%   state.pos = [y; z], state.vel = [y_dot; z_dot], state.rot = [phi],
%   state.omega = [phi_dot]
%
%   des_state: The desired states are:
%   des_state.pos = [y; z], des_state.vel = [y_dot; z_dot], des_state.acc =
%   [y_ddot; z_ddot]
%
%   params: robot parameters

%   Using these current and desired states, you have to compute the desired
%   controls

p = calcP(des_state.pos(1), state.pos(1), 1);
% p = calcP(.1, state.pos(1), 1);
d = calcD(des_state.vel(1), state.vel(1), 1);
o = (p + d + des_state.acc(2)) * (-1/params.gravity);

p = calcP(o, state.rot, 3);
d = calcD(0, state.omega, 3);
u2 = params.Ixx * (p + d);

p = calcP(des_state.pos(2), state.pos(2), 2);
% p = calcP(1, state.pos(2), 2);
d = calcD(des_state.vel(2), state.vel(2), 2);
% d = calcD(1, state.vel(2), 2);
u1 = params.mass * (des_state.acc(2) + params.gravity + d + p);

F = u1;
M = u2;

end

function [val] = calcP(des_pos, pos, var)
%      y, z, o
% K_p = [20, 800, 0];
% K_p = [1000, 800, 400];
K_p = [3,4500, 100];

val = K_p(var) * (des_pos - pos);
end

function [val] = calcD(des_pos, pos, var) 
%      y, z, o
% K_d = [0, 260, 111];
% K_d = [3.75, 150, 0];
K_d = [1, 20 , 4];

val = K_d(var) * (des_pos - pos);
end

