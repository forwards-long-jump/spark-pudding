function getRequiredComponents()
  return {"position"}
end

function update()
  for i, e in ipairs(entities) do
    if e.position.x < -100 or e.position.x > 1300 then
      e._meta:delete()
    end
  end
end
    