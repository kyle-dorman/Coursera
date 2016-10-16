function [ u ] = pd_controller(~, s, s_des, params)
%PD_CONTROLLER  PD controller for the height
%
%   s: 2x1 vector containing the current state [z; v_z]
%   s_des: 2x1 vector containing desired state [z; v_z]
%   params: robot parameters

kP = 80;
kV = 7;

g = params.gravity;
m = params.mass;

e = s_des(1) - s(1);
e_dot = s_des(2) - s(2);

u = calcThrust(m, g, e, e_dot, kP, kV); 

end

function [ u ] = calcThrust(m, g, e, e_dot, kP, kV)

u = (kP * e + kV * e_dot + g) * m;

end

